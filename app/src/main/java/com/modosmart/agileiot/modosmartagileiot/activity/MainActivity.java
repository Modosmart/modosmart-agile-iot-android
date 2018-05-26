package com.modosmart.agileiot.modosmartagileiot.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.modosmart.agileiot.modosmartagileiot.R;
import com.modosmart.agileiot.modosmartagileiot.fragment.AboutFragment;
import com.modosmart.agileiot.modosmartagileiot.fragment.BleFragment;
import com.modosmart.agileiot.modosmartagileiot.fragment.WiFiFragment;
import com.modosmart.agileiot.modosmartagileiot.utils.ConstantsUtils;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // change your content accordingly.
        WiFiFragment wifiFragment = (WiFiFragment) fragmentManager.findFragmentByTag(ConstantsUtils.FRAGMENT_WIFI_TAG);
        if (wifiFragment == null) {
            // Never clicked before
            wifiFragment = new WiFiFragment();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, wifiFragment, ConstantsUtils.FRAGMENT_WIFI_TAG);
        fragmentTransaction.addToBackStack(ConstantsUtils.FRAGMENT_WIFI_TAG);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_wifi) {
            // change your content accordingly.
            WiFiFragment wifiFragment = (WiFiFragment) fragmentManager.findFragmentByTag(ConstantsUtils.FRAGMENT_WIFI_TAG);
            if (wifiFragment == null) {
                // Never clicked before
                wifiFragment = new WiFiFragment();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, wifiFragment, ConstantsUtils.FRAGMENT_WIFI_TAG);
            fragmentTransaction.addToBackStack(ConstantsUtils.FRAGMENT_WIFI_TAG);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        } else if (item.getItemId() == R.id.navigation_ble) {
            // change your content accordingly.
            BleFragment bleFragment = (BleFragment) fragmentManager.findFragmentByTag(ConstantsUtils.FRAGMENT_BLE_TAG);
            if (bleFragment == null) {
                // Never clicked before
                bleFragment = new BleFragment();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, bleFragment, ConstantsUtils.FRAGMENT_BLE_TAG);
            fragmentTransaction.addToBackStack(ConstantsUtils.FRAGMENT_BLE_TAG);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        } else if (item.getItemId() == R.id.navigation_about) {
            // change your content accordingly.
            AboutFragment aboutFragment = (AboutFragment) fragmentManager.findFragmentByTag(ConstantsUtils.FRAGMENT_ABOUT_TAG);
            if (aboutFragment == null) {
                // Never clicked before
                aboutFragment = new AboutFragment();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, aboutFragment, ConstantsUtils.FRAGMENT_ABOUT_TAG);
            fragmentTransaction.addToBackStack(ConstantsUtils.FRAGMENT_ABOUT_TAG);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
