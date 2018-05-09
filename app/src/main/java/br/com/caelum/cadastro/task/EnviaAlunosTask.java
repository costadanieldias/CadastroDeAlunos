package br.com.caelum.cadastro.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.cadastro.support.WebClient;

/**
 * Created by android5497 on 09/09/15.
 */
public class EnviaAlunosTask extends AsyncTask<Object, Object, String > {
    private Context context;
    private ProgressDialog dialog; // exibe mensagem na tela enquanto executa o a thread

    public EnviaAlunosTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Aguarde...", "Envio de dados para a web", true, true);
    }

//  EXECUTADO NA SEGUNDA THREAD
    @Override
    protected String doInBackground(Object... params) {
        return new WebClient().post((String) params[0]);
    }


//  Implementar caso a resposta seja necessaria THREAD PRINCIPAL
    @Override
    protected  void  onPostExecute(String result) {

        try {
            JSONObject resultObject = new JSONObject(result);
            Double mediaNota = resultObject.getDouble("media");
            String quantidade = resultObject.getString("quantidade");
            Toast.makeText(context, "A nota média dos " + quantidade + " alunos é de " + mediaNota.toString() + ".", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.dismiss();
    }
}
