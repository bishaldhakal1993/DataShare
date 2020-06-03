package com.example.datashare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    WifiManager wifiManager;
    TextView wifiStatusTextView;
    Switch wifiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (showWritePermissionSettings()){
            Toast.makeText(MainActivity.this, "Allowed", Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Defining Variables
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiStatusTextView = (TextView) findViewById(R.id.wifiStateText);
        /*wifiSwitch = (Switch) findViewById(R.id.switchWifi);

        /* Checking wifi state.
         * If wifi is enabled, display "wifi is on" and set toggle button to on position.
         * If Wifi is disabled, display "wifi is off" and set toggle button to off position.
         */
        /*if (wifiManager.isWifiEnabled()) {
            wifiStatusTextView.setText("Wifi is currently ON");
            wifiSwitch.setChecked(true);
        } else {
            wifiStatusTextView.setText("Wifi is currently OFF");
            wifiSwitch.setChecked(false);
        }

        /* adds listener to toggle button
         * If toggle is checked, wifi is enabled and "Wifi is on" is displayed.
         * If toggle is unchecked, wifi is enabled and "Wifi is off" is displayed.
         */
        /*wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiStatusTextView.setText("Wifi is currently ON");
                    Toast.makeText(MainActivity.this, "Wifi may take a moment to turn on", Toast.LENGTH_LONG).show();
                } else {
                    wifiManager.setWifiEnabled(false);
                    wifiStatusTextView.setText("Wifi is currently OFF");
                }
            }
        });*/
    }

    public void shareWifi(View view){
        Intent intent = new Intent(MainActivity.this, HotspotActivity.class);
        startActivity(intent);
    }

    public void connectWifi(View view){
        Intent intent = new Intent(MainActivity.this, WifiActivity.class);
        startActivity(intent);
    }
    public void searchWifiList(View view){
        ApManager.configApState(MainActivity.this); // change Ap state :boolean
        /*if(ApManager.isApOn(MainActivity.this)){
            Toast.makeText(MainActivity.this, "Yesss", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "Nooooo", Toast.LENGTH_LONG).show();
        }*/

        //Intent intent = new Intent(MainActivity.this, SsidActivity.class);
        //startActivity(intent);
    }

    private boolean showWritePermissionSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (!Settings.System.canWrite(this)) {
                Log.v("DANG", " " + !Settings.System.canWrite(this));
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return false;
            }
        }
        return true; //Permission already given
    }
}
