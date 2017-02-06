package ua.kiev.sergiosiniy.smsfilter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.services.SMSFilterService;

public class HomeFragment extends Fragment {
    private final String TAG = "Service actions";
    private Intent serviceIntent;
    private  Context context;



    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT<23) {
            context = getActivity();
        }else{
            context=getContext();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if(view!=null) {
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_start_service);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!SMSFilterService.SERVICE_STATUS) {
                        runSMSFilterService();
                    } else {
                        stopSMSFilterService();
                    }
                }
            });
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                fab.setElevation(4);
            }
        }


    }

    private void runSMSFilterService(){

        serviceIntent = new Intent(context, SMSFilterService.class);
        context.startService(serviceIntent);
        SMSFilterService.SERVICE_STATUS=true;

        Log.i(TAG,"is running");
    }

    private void stopSMSFilterService(){
        context.stopService(serviceIntent);
        SMSFilterService.SERVICE_STATUS=false;

        Log.i(TAG,"is stoped");
    }
}
