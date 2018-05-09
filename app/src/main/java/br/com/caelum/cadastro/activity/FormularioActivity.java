package br.com.caelum.cadastro.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import br.com.caelum.cadastro.BuildConfig;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.helper.FormularioHelper;
import br.com.caelum.cadastro.util.Constants;
import br.com.caelum.cadastro.vo.Aluno;


public class FormularioActivity extends AppCompatActivity {

    FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Intent edicao = super.getIntent();
        Aluno aluno = (Aluno) edicao.getSerializableExtra(Constants.ALUNO);

        helper = new FormularioHelper(this);

        if(aluno != null) {
            helper.preencherFormulario(aluno);
        }

//        Button botaoSalvar = (Button) this.findViewById(R.id.formulario_salvar);
//        botaoSalvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        Button botaoFoto = helper.getFotoButton();
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) +"/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FormularioActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        arquivoFoto));
                startActivityForResult(intentFoto, Constants.TIRAR_FOTO);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//          return true;
//        }

        switch(item.getItemId()) {
//            case R.id.action_settings:
//                do nothing;
            case R.id.menu_salvar:
                Aluno aluno = helper.getAluno();
                AlunoDAO dao = new AlunoDAO(this);
                if(aluno.getId() == null) {
                    if(helper.temNome()) {
                        dao.inserir(aluno);
                        Toast.makeText(FormularioActivity.this, Constants.O_ALUNO.concat(aluno.getNome()).concat(Constants.FOI_CADASTRADO_COM_SUCESSO)
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        helper.mostrarErro();
                    }
                } else {
                    if(helper.temNome()) {
                        dao.alterar(aluno);
                        Toast.makeText(FormularioActivity.this, Constants.O_ALUNO.concat(aluno.getNome()).concat(Constants.FOI_ALTERADO_COM_SUCESSO)
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        helper.mostrarErro();
                    }
                }
                dao.close(); //importante sempre fechar a conexão
                finish();
                return true;

            case R.id.voltar:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.TIRAR_FOTO) {
            if(resultCode == Activity.RESULT_OK) {
                helper.carregarFoto(caminhoFoto);
            } else {
                caminhoFoto = null;
            }
        }
    }

    // Guarda variável no bundle, que é um mapa de obj a nível de aplicação
    @Override
    protected  void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("caminho", caminhoFoto);
    }

    // Recupera uma variável apos o android ter destruído o a aplicação
    @Override
    protected  void onRestoreInstanceState(Bundle bundle) {
        caminhoFoto = (String) bundle.getSerializable("caminho");
        helper.carregarFoto(caminhoFoto);
    }
}
