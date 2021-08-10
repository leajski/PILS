package com.pils.disconnect;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    private Button Disconnect;
    private NotificationManager mNotificationManager;
    private Button Partie;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Forcer le mode ne pas déranger
        Partie = (Button) findViewById(R.id.partie);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(mNotificationManager.isNotificationPolicyAccessGranted()){
            mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_NONE);
            Context context = getApplicationContext();
            CharSequence text = "Le mode ne pas déranger est activé";
            int duration = Toast.LENGTH_LONG ;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Pour fonctionner, DYSCO doit avoir accès au contrôle du mode 'ne pas déranger'";
            int duration = Toast.LENGTH_LONG ;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }

        Partie.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, choix_partie.class));
            }
        });
    }

    protected void changeInterruptionFiler(int interruptionFilter){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){ // If api level minimum 23

            // If notification policy access granted for this package
            if(mNotificationManager.isNotificationPolicyAccessGranted()){

                // Set the interruption filter
                mNotificationManager.setInterruptionFilter(interruptionFilter);
            }else {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);

            }
        }
    }
}
