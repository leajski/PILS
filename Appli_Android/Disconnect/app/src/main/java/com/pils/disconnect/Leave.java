package com.pils.disconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class Leave extends AppCompatActivity {

    private TextView scorejoueur;
    private TextView gagefin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        scorejoueur = findViewById(R.id.scorejoueur);
        gagefin = findViewById(R.id.gagefin);
        scorejoueur.setMovementMethod(new ScrollingMovementMethod());

        final partieHandler ph = (partieHandler) getIntent().getSerializableExtra("partieHandler");

        String w = ph.getScoreFinal();
        String x = ph.getGage();
        String[] listescore = w.split(";");
        String f = "";
        for(int i=0;i<listescore.length;i++){
            f += listescore[i]+"\n";
        }
        scorejoueur.setText(f);
        gagefin.setText(x);

        Button homeButton = (Button) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
