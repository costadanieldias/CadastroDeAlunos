package br.com.caelum.cadastro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.util.Constants;

/**
 * Created by android5497 on 08/09/15.
 */
public class SMSReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        // Obtém o PDU com a mensagem
        Bundle bundle = intent.getExtras();
        Object[] mensagens = (Object[]) bundle.get(Constants.PDUS);

        // Pega a PDU mais recente
        byte[] mensagem = (byte[]) mensagens[0];

        SmsMessage sms = SmsMessage.createFromPdu(mensagem);

        String telefone = sms.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);
        if(dao.isAluno(telefone)) {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
            Toast.makeText(context, Constants.CHEGOU_UM_SMS, Toast.LENGTH_SHORT).show();
        }


    }
}
