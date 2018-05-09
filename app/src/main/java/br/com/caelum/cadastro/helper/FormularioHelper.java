package br.com.caelum.cadastro.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.util.Constants;
import br.com.caelum.cadastro.vo.Aluno;

/**
 * Created by android5497 on 02/09/15.
 */
public class FormularioHelper {
    private Activity activity;
    private Aluno aluno;

    private EditText nome;
    private EditText endereco;
    private EditText telefone;
    private EditText site;
    private RatingBar nota;
    private ImageView foto;
    private Button fotoButton;

    public FormularioHelper(Activity activity) {
        this.aluno = new Aluno();
        this.nome = (EditText) activity.findViewById(R.id.fomulario_nome);
        this.endereco = (EditText) activity.findViewById(R.id.fomulario_endereco);
        this.telefone = (EditText) activity.findViewById(R.id.fomulario_telefone);
        this.site = (EditText) activity.findViewById(R.id.fomulario_site);
        this.nota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        this.foto = (ImageView) activity.findViewById(R.id.formulario_foto);
        this.fotoButton = (Button) activity.findViewById(R.id.formulario_foto_button);
    }

    public Aluno getAluno(){
        aluno.setNome(nome.getText().toString());
        aluno.setEndereco(endereco.getText().toString());
        aluno.setTelefone(telefone.getText().toString());
        aluno.setSite(site.getText().toString());
        aluno.setNota(Double.valueOf(nota.getProgress()));
        aluno.setCaminhoFoto((String) foto.getTag());

        return aluno;
    }

    public void preencherFormulario(Aluno aluno) {
        nome.setText(aluno.getNome());
        endereco.setText(aluno.getEndereco());
        telefone.setText(aluno.getTelefone());
        site.setText(aluno.getSite());
        nota.setProgress(aluno.getNota().intValue());
        if(aluno.getCaminhoFoto() != null) {
            this.carregarFoto(aluno.getCaminhoFoto());
        }
        this.aluno = aluno;
    }

    public boolean temNome() {
        return !nome.getText().toString().isEmpty();
    }

    public void mostrarErro() {
        nome.setError(Constants.CAMPO_NOME_DEVE_SER_PREENCHIDO);
    }

    public Button getFotoButton() {
        return fotoButton;
    }

    public void carregarFoto(String caminhoFoto) {
        if(caminhoFoto != null && !caminhoFoto.isEmpty()) {
            Bitmap fotoOriginal = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap fotoReduzida = Bitmap.createScaledBitmap(fotoOriginal, 300, 300, true);
            this.foto.setImageBitmap(fotoReduzida);
            this.foto.setTag(caminhoFoto);
            this.foto.setScaleType(ScaleType.FIT_XY);
        }
    }
}
