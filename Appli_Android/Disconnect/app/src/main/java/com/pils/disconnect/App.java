package com.pils.disconnect;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private boolean isBlackScreen = false;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        /*if (++activityReferences == 1 && !isActivityChangingConfigurations) {
                // App enters foreground
                Context context = getApplicationContext();
                CharSequence text = activity.getClass().getSimpleName();
                int duration = Toast.LENGTH_SHORT ;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


        }*/
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    /*
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // If the screen is off then the device has been locked
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean isScreenOn = powerManager.isScreenOn();
            if (!isScreenOn) {
                // The screen has been locked
                Context context = getApplicationContext();
                CharSequence text = "Phone has been locked";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            if (isScreenOn){
                // App enters background
                Context context = getApplicationContext();
                CharSequence text = "Bye Bye";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

    */
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
