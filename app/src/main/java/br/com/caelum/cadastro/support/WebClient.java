package br.com.caelum.cadastro.support;

/*import org.apache.http.params.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;*/
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by android5497 on 09/09/15.
 */
public class WebClient {
//    private static final String URL = "http://www.caelum.com.br/mobile";
//
//    public String post(String json) {
//        try {
//            HttpPost post = new HttpPost(URL);
//            post.setEntity(new StringEntity(json));
//            post.setHeader("Accept", "application/json");
//            post.setHeader("Content-type", "application/json");
//
//            DefaultHttpClient client = new DefaultHttpClient();
//            HttpResponse response = client.execute(post);
//            return EntityUtils.toString(response.getEntity());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "Ocorreu um erro!";
//    }

    public String post(String json) {
        try {
            URL url = new URL("https://www.caelum.com.br/mobile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");

            //coloca o json no corpo do POST
            connection.setDoOutput(true);
            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(json);

            connection.connect();

            // envia para o servidor e trata resposta (que no nosso caso só tem uma linha)
            String jsonDeResposta = new Scanner(connection.getInputStream()).next();
            return jsonDeResposta;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
