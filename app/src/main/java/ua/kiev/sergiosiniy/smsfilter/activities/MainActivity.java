package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.services.SMSFilterService;


public class MainActivity extends AppCompatActivity {

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_start_service);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRunning) {
                    runSMSFilterService();
                }else{
                    stopSMSFilterService();
                }
            }
        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(4);
        }
    }

    private void runSMSFilterService(){
        Intent serviceIntent = new Intent(this, SMSFilterService.class);
        startService(serviceIntent);
        isRunning=true;
    }

    private void stopSMSFilterService(){
        Intent serviceIntent = new Intent(this, SMSFilterService.class);
        stopService(serviceIntent);
        isRunning=false;
    }
}
