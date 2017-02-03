package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.services.SMSFilterService;


public class MainActivity extends AppCompatActivity {


    private Intent serviceIntent;
    private DrawerLayout mDrawerLayout;
    private Intent switchActivity;
    private final String TAG = "Service actions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarSetup();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_start_service);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SMSFilterService.SERVICE_STATUS) {
                    runSMSFilterService();
                }else{
                    stopSMSFilterService();
                }
            }
        });
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(4);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if(navigationView!=null){
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                setServiceStatusNavigationHeader(SMSFilterService.SERVICE_STATUS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runSMSFilterService(){
        serviceIntent = new Intent(this, SMSFilterService.class);
        startService(serviceIntent);
        SMSFilterService.SERVICE_STATUS=true;

        Log.i(TAG,"is running");
    }

    private void stopSMSFilterService(){
        stopService(serviceIntent);
        SMSFilterService.SERVICE_STATUS=false;

        Log.i(TAG,"is stoped");
    }

    private void toolbarSetup(){
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(actionBarToolbar);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            actionBarToolbar.setElevation(4);
        }

    }

    private void setServiceStatusNavigationHeader(boolean status){
        if(!status){
            ImageView serviceStatusImg =
                    (ImageView) findViewById(R.id.navigation_header_service_status_img);
            TextView serviceStatus = (TextView) findViewById(R.id.navigation_header_service_status);
            serviceStatusImg.setImageResource(R.mipmap.is_down);
            serviceStatus.setText(getString(R.string.is_down));
        }else{
            ImageView serviceStatusImg =
                    (ImageView) findViewById(R.id.navigation_header_service_status_img);
            TextView serviceStatus = (TextView) findViewById(R.id.navigation_header_service_status);
            serviceStatusImg.setImageResource(R.mipmap.is_running);
            serviceStatus.setText(getString(R.string.is_running));
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch(menuItem.getTitle().toString()){
                            case "Home":
                                mDrawerLayout.closeDrawers();
                                return true;
                            case "Filtered words":
                                mDrawerLayout.closeDrawers();
                                switchActivity = new Intent(getApplicationContext(),
                                        FilteredWords.class);
                                startActivity(switchActivity);
                                return true;
                            case "Quarantined":
                                mDrawerLayout.closeDrawers();
                                switchActivity = new Intent(getApplicationContext(),
                                        Quarantined.class);
                                startActivity(switchActivity);
                                return true;
                            case "Exceptions":
                                mDrawerLayout.closeDrawers();
                                switchActivity = new Intent(getApplicationContext(),
                                        ExceptPhoneNum.class);
                                startActivity(switchActivity);
                                return true;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }

                });
    }
}
