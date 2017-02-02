package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.services.SMSFilterService;


public class MainActivity extends AppCompatActivity {

    private boolean isRunning = false;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarSetup();
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
        serviceIntent = new Intent(this, SMSFilterService.class);
        startService(serviceIntent);
        isRunning=true;
    }

    private void stopSMSFilterService(){
        stopService(serviceIntent);
        isRunning=false;
    }

    private void toolbarSetup(){
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        actionBarToolbar.setNavigationIcon(R.mipmap.ic_menu_white_36dp);
        actionBarToolbar.setNavigationContentDescription(R.string.navigation_icon_description);
        actionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setSupportActionBar(actionBarToolbar);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            actionBarToolbar.setElevation(4);
        }

    }
}
