package com.pils.disconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choix_partie extends AppCompatActivity {

    private Button Creer;
    private Button Rejoindre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_partie);

        Creer = (Button) findViewById(R.id.creer);
        Rejoindre = (Button) findViewById(R.id.rejoindre);
        Creer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(choix_partie.this, ParametresPartie.class));
            }
        });


        Rejoindre.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(choix_partie.this, Rejoindre.class));
            }
        });

    }
}