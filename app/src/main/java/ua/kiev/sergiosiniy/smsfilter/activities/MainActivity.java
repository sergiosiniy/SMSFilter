package ua.kiev.sergiosiniy.smsfilter.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kiev.sergiosiniy.smsfilter.R;
import ua.kiev.sergiosiniy.smsfilter.services.SMSFilterService;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Bundle listType;
    private RecyclerViewFragment recyclerViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarSetup();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        switchFragment(new HomeFragment());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                setServiceStatusNavigationHeader(SMSFilterService.SERVICE_STATUS);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.activity_main_fragment_placeholder, fragment);
        fragmentTransaction.commit();

    }

    private void toolbarSetup() {
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(actionBarToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBarToolbar.setElevation(4);
        }

    }

    private void setServiceStatusNavigationHeader(boolean status) {
        if (!status) {
            ImageView serviceStatusImg =
                    (ImageView) findViewById(R.id.navigation_header_service_status_img);
            TextView serviceStatus = (TextView) findViewById(R.id.navigation_header_service_status);
            serviceStatusImg.setImageResource(R.mipmap.is_down);
            serviceStatus.setText(getString(R.string.is_down));
        } else {
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
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_home:
                                switchFragment(new HomeFragment());
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.navigation_filtered:
                                mDrawerLayout.closeDrawers();
                                listType = new Bundle();
                                listType.putInt(RecyclerViewFragment.ITEM_ID, menuItem.getItemId());
                                recyclerViewFragment = new RecyclerViewFragment();
                                recyclerViewFragment.setArguments(listType);
                                switchFragment(recyclerViewFragment);
                                return true;
                            case R.id.navigation_quarantined:
                                mDrawerLayout.closeDrawers();
                                listType = new Bundle();
                                listType.putInt(RecyclerViewFragment.ITEM_ID, menuItem.getItemId());
                                recyclerViewFragment = new RecyclerViewFragment();
                                recyclerViewFragment.setArguments(listType);
                                switchFragment(recyclerViewFragment);
                                return true;
                            case R.id.navigation_exceptions:
                                mDrawerLayout.closeDrawers();
                                listType = new Bundle();
                                listType.putInt(RecyclerViewFragment.ITEM_ID, menuItem.getItemId());
                                recyclerViewFragment = new RecyclerViewFragment();
                                recyclerViewFragment.setArguments(listType);
                                switchFragment(recyclerViewFragment);
                                return true;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
