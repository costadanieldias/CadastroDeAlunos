package br.com.caelum.cadastro.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.vo.Aluno;

/**
 * Created by android5497 on 08/09/15.
 */
public class ListaAlunoAdapter extends BaseAdapter {
    private List<Aluno> alunoList;
    private Activity activity;

    public ListaAlunoAdapter(List<Aluno> alunoList, Activity activity) {
        this.alunoList = alunoList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return alunoList.size();
    }

    @Override
    public Object getItem(int position) {
        return alunoList.get(position);
    }

    @Override
    public long getItemId(int position) { // Pode ser qualquer valor do tipo long q represente algo relevangte
        return alunoList.get(position).getId().longValue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        // Infla a view ou reaproveita uma view existente
        if(convertView == null) {
            view = this.activity.getLayoutInflater().inflate(R.layout.item, parent, false);
        } else {
            view = convertView;
        }

        Aluno aluno = this.alunoList.get(position);

        // Obtendo nome do aluno
        TextView nome = (TextView) view.findViewById(R.id.item_nome);
        nome.setText(aluno.toString());

        // Obtendo telefone do aluno
        if(view.findViewById(R.id.item_telefone) != null) {
            TextView telefone = (TextView) view.findViewById(R.id.item_telefone);
            telefone.setText(aluno.getTelefone());
        }
        // Obtendo site do aluno
        if(view.findViewById(R.id.item_site) != null) {
            TextView site = (TextView) view.findViewById(R.id.item_site);
            site.setText(aluno.getSite());
        }
        // Obtendo a foto
        Bitmap bitmap;

        if(aluno.getCaminhoFoto() != null) {
            bitmap = BitmapFactory.decodeFile(aluno.getCaminhoFoto());
        } else {
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_no_image);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        ImageView foto = (ImageView) view.findViewById(R.id.item_foto);
        foto.setImageBitmap(bitmap);

        return view;
    }
}
