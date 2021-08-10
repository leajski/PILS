package com.pils.disconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class attenteconnexion extends AppCompatActivity {

    private Button attentehote;
    private Button lancerpartie;
    private TextView varjoueur;
    private TextView varpartie;
    private TextView listejoueurs;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean started = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attenteconnexion);

        attentehote = (Button) findViewById(R.id.attentehote);
        lancerpartie = (Button) findViewById(R.id.lancerpartie);
        varjoueur = findViewById(R.id.varjoueur);
        varpartie = findViewById(R.id.varpartie);
        listejoueurs = findViewById(R.id.nomsjoueurs);
        listejoueurs.setMovementMethod(new ScrollingMovementMethod());

        final partieHandler ph = (partieHandler) getIntent().getSerializableExtra("partieHandler");

        varpartie.setText(Integer.toString(ph.getPartie_ID()));
        varjoueur.setText(Integer.toString(ph.getNbjoueur()));

        final socketHandler sh = new socketHandler();
        sh.syncpartieHandler(ph);
        Thread t = new Thread(sh);
        t.start();
        while(sh.getIsInit()==false){
        }
        if(sh.getPh().getIsHost() == false){
            lancerpartie.setVisibility(View.GONE);
        }else {
            attentehote.setVisibility(View.GONE);
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                varjoueur.setText(Integer.toString(sh.getPh().getNbjoueur()));
                String x = sh.getPh().getListedesjouers();
                listejoueurs.setText(x);
                if (sh.getIsStarted() == true){
                    Intent i = new Intent(com.pils.disconnect.attenteconnexion.this, BlackScreen.class);
                    i.putExtra("partieHandler", sh.getPh());
                    sh.setRunning(false);
                    while(sh.getRunning() == true){
                    }
                    startActivity(i);
                } else {
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.postDelayed(r,500);

        lancerpartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sh.send("START:");
                    }
                }).start();
            }
        });
        };
    }