package com.pils.disconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ParametresPartie extends AppCompatActivity {

    private Button Creer;
    //private Button Maison;
    //private Button Bar;
    private EditText Gage;
    private TextView Instruction_Gage;
    //private TextView Info_bar;
    private TextView Instruction_Nickname;
    private EditText Nickname;
    //private boolean atHome;
    private TextView Carac;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres_partie);

        Creer = (Button) findViewById(R.id.creer2);
        //Maison =(Button) findViewById(R.id.maison);
        //Bar = (Button) findViewById(R.id.bar);
        Gage = (EditText) findViewById(R.id.gage);
        Instruction_Gage = (TextView) findViewById(R.id.instruction_gage);
        //Info_bar = (TextView) findViewById(R.id.info_mode_bar);
        Instruction_Nickname = (TextView) findViewById(R.id.instruction_nickname) ;
        Nickname = (EditText) findViewById(R.id.nicknamecreer);
        Carac = (TextView) findViewById(R.id.contraintecaracterecreer);

        //Maison.setOnClickListener(new View.OnClickListener(){
        //    public void onClick(View v){
        //        atHome = true;
                Creer.setVisibility(View.VISIBLE);
                Instruction_Nickname.setVisibility(View.VISIBLE);
                Nickname.setVisibility(View.VISIBLE);
                Carac.setVisibility(View.VISIBLE);
                Gage.setVisibility(View.VISIBLE);
        //        Info_bar.setVisibility(View.INVISIBLE);
                Instruction_Gage.setVisibility(View.VISIBLE);
        //        Maison.setBackgroundResource(R.drawable.button_rounded_green);
        //        Bar.setBackgroundResource(R.drawable.button_rounded_black);
        //    }
        //});

        Creer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if(Nickname.getText().toString().matches("") || Gage.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Veuillez choisir un pseudo et un gage pour créer une partie",Toast.LENGTH_SHORT).show();
                }else if(Nickname.getText().toString().matches("[a-zA-Z0-9]+") == false || Nickname.getText().toString().length()>15) {
                    Toast.makeText(getApplicationContext(), "Veuillez choisir une pseudo valide (attention aux caractères utilisés et à la longueur)", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        final socketHandler sh = new socketHandler();
                        partieHandler ph = new partieHandler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sh.setSocket();
                            }
                        }).start();
                        while (sh.getSocketInit() == false) {
                        }
                        ph.setGage(Gage.getText().toString());
                        ph.setNickname(Nickname.getText().toString());
                        //ph.setModeBar(atHome);
                        sh.syncpartieHandler(ph);
                        Thread t = new Thread(sh);
                        t.start();
                        while (sh.getIsInit() == false) {
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sh.send("CREATE:"+Nickname.getText().toString()+"&"+Gage.getText().toString());
                            }
                        }).start();
                        while (sh.getPh().getIsConfirm() == false) {
                        }
                        Intent intent = new Intent(com.pils.disconnect.ParametresPartie.this, attenteconnexion.class);
                        intent.putExtra("partieHandler", sh.getPh());
                        sh.setRunning(false);
                        while (sh.getRunning() == true) {
                        }
                        startActivity(intent);
                    } catch (Exception e) {
                        System.err.println("Creer (err): " + e);
                    }
                }
            }
        });
    }
}
