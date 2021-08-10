package com.pils.disconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Rejoindre extends AppCompatActivity {

    private Button Join;
    private EditText num;
    private EditText Nickname;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoindre);

        Join = (Button) findViewById(R.id.rejoindre);
        num = (EditText) findViewById(R.id.num_partie);
        Nickname = (EditText) findViewById(R.id.nicknamerejoindre);

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Nickname.getText().toString().matches("") || num.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Veuillez choisir un pseudo et un numéro de partie pour rejoindre",Toast.LENGTH_SHORT).show();
                }else if(Nickname.getText().toString().matches("[a-zA-Z0-9]+") == false || Nickname.getText().toString().length()>15) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir une pseudo valide (attention aux caractères utilisés et à la longueur)", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        final socketHandler sh = new socketHandler();
                        partieHandler ph = new partieHandler();
                        ph.setNickname(Nickname.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sh.setSocket();
                            }
                        }).start();
                        while(sh.getSocketInit() == false){

                        }
                        sh.syncpartieHandler(ph);
                        Thread t = new Thread(sh);
                        t.start();
                        while(sh.getIsInit() == false) {
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sh.send("JOIN:"+num.getText().toString()+":"+Nickname.getText().toString());
                            }
                        }).start();
                        while(sh.getTentativeJoin() == false){
                        }
                        if(sh.getBonnePartie() == false){
                            Toast.makeText(getApplicationContext(),"Impossible de rejoindre cette partie",Toast.LENGTH_SHORT).show();
                            sh.setBonnePartie(true);
                            sh.setTentativeJoin(false);
                        } else {
                            sh.setTentativeJoin(false);
                            while(sh.getPh().getIsJoin() == false) {
                            }
                            Intent intent = new Intent(com.pils.disconnect.Rejoindre.this, attenteconnexion.class);
                            sh.getPh().setIsHost(false);
                            sh.getPh().setPartie_ID(Integer.parseInt(num.getText().toString()));
                            intent.putExtra("partieHandler",sh.getPh());
                            sh.setRunning(false);
                            while(sh.getRunning() == true){
                            }
                            startActivity(intent);
                        }
                    } catch (Exception e){
                        System.err.println(e);
                    }
                }
            }
        });

    }


}