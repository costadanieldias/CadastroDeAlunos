package br.com.caelum.cadastro.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.adapter.ListaAlunoAdapter;
import br.com.caelum.cadastro.converter.AlunoConverter;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.support.WebClient;
import br.com.caelum.cadastro.task.EnviaAlunosTask;
import br.com.caelum.cadastro.util.Constants;
import br.com.caelum.cadastro.vo.Aluno;
import br.com.caelum.cadastro.activity.ProvasActivity;


public class ListaAlunosActivity extends AppCompatActivity {
    private ListView listaAlunos;

    private List<Aluno> alunos;

//  Exibe o nível da bateria sempre que o mesmo for alterado
    private BroadcastReceiver bateria = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int valor = intent.getIntExtra("level", 0);
            Toast.makeText(context, valor + "%", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        this.listaAlunos = (ListView) findViewById(R.id.lista_alunos);

        this.carregarLista();

        ListaAlunoAdapter adapter = new ListaAlunoAdapter(alunos, this);

        listaAlunos.setAdapter(adapter);

        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() { // classe anonima
            @Override // sempre usar @override para evitar que acabe não concatenando o comportamento da super classe
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = (Aluno) parent.getItemAtPosition(position);
                Intent edicao = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                edicao.putExtra(Constants.ALUNO, aluno);
                startActivity(edicao);
            }
        });

//        listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // classe anonima
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ListaAlunosActivity.this, "Posição clicada: " + position, Toast.LENGTH_SHORT).show();
//                return false; // Se true consome o evento não invocando o método onItemClick
//            }
//        });

        Button botaoNovo = (Button) this.findViewById(R.id.novo_aluno);

        botaoNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(this.listaAlunos); // seta o menu de contexto para a view

        registerReceiver(bateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)); //  Exibe o nível da bateria sempre que o mesmo for alterado
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_alunos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.menu_enviar_notas:
                AlunoDAO dao = new AlunoDAO(this);
                List<Aluno> alunos = dao.getLista();
                dao.close();
                String json = new AlunoConverter().toJSON(alunos);
                new EnviaAlunosTask(this).execute(json);
                return true;

            case R.id.menu_receber_provas:
                Intent provas = new Intent(this, ProvasActivity.class);
                startActivity(provas);
                return true;

            case R.id.menu_mapa:
                Intent mapa = new Intent(this, MostraAlunosActivity.class);
                startActivity(mapa);
                return true;
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, final ContextMenuInfo menuInfo) {  // ContextMenuInfo sabe identificar qual view foi clicada

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem ligar = menu.add(Constants.LIGAR);
        MenuItem sms = menu.add(Constants.ENVIAR_SMS);
        MenuItem mapa = menu.add(Constants.ACHAR_NO_MAPA);
        MenuItem site = menu.add(Constants.NAVEGAR_NO_SITE);
        MenuItem delecao = menu.add(Constants.DELETAR); // alternativa ao getMenuInflater().inflate()

//      Ligar
        Intent intentLigar = new Intent(Intent.ACTION_DIAL);
        intentLigar.setData(Uri.parse(Constants.TEL + aluno.getTelefone()));
        ligar.setIntent(intentLigar);

//      Enviar SMS
        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse(Constants.SMS + aluno.getTelefone()));
        intentSms.putExtra(Constants.SMS_BODY, Constants.MENSAGEM);
        sms.setIntent(intentSms);

//      Abrir site
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        String siteAluno = aluno.getSite();
        if(!siteAluno.startsWith(Constants.HTTP)) {
            siteAluno = Constants.HTTP.concat(aluno.getSite());
        }
        intentSite.setData(Uri.parse(siteAluno));
        site.setIntent(intentSite);

        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        String endereco = aluno.getEndereco();
        intentMapa.setData(Uri.parse("geo:0,0?z=14&q=" + Uri.encode(endereco)));
        mapa.setIntent(intentMapa);

//      Deletar o registro
        delecao.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
/*            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deletar(aluno);
                dao.close();
                carregarLista();
                return false;
            }*/

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                new AlertDialog.Builder(ListaAlunosActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(Constants.DELETAR)
                        .setMessage(Constants.DESEJA_REALMENTE_DELETAR)
                        .setPositiveButton(Constants.SIM,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                                        dao.deletar(aluno);
                                        dao.close();
                                        carregarLista();
                                    }
                                }).setNegativeButton(Constants.NAO, null).show();

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.carregarLista();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bateria);
    }

    private void carregarLista() {
        AlunoDAO dao = new AlunoDAO(this);
        this.alunos = dao.getLista();
        dao.close();

//        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        ListaAlunoAdapter adapter = new ListaAlunoAdapter(alunos, this);
        this.listaAlunos.setAdapter(adapter);
    }
}
