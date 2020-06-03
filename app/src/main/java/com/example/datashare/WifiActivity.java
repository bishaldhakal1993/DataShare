package com.example.datashare;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WifiActivity extends AppCompatActivity {
    String networkSSID = "DataShareWifi";
    String networkPass = "connect2me";
    Switch switchWifi;
    TextView wifiStateText;
    WifiManager wifiManager;

    WifiConfiguration conf = new WifiConfiguration();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        if(ApManager.isApOn(WifiActivity.this)){
            ApManager.configApState(WifiActivity.this);
        }
        conf.SSID = "\"" + networkSSID + "\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        switchWifi = (Switch) findViewById(R.id.switchWifi);
        wifiStateText = (TextView) findViewById(R.id.wifiStateText);
        if (wifiManager.isWifiEnabled()) {
            wifiStateText.setText("Wifi is currently ON");
            switchWifi.setChecked(true);
        } else {
            wifiStateText.setText("Wifi is currently OFF");
            switchWifi.setChecked(false);
        }
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(ApManager.isApOn(WifiActivity.this)){
                        ApManager.configApState(WifiActivity.this);
                    }
                    wifiManager.setWifiEnabled(true);
                    List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                    for (WifiConfiguration i : list) {
                        if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                            wifiManager.disconnect();
                            wifiManager.enableNetwork(i.networkId, true);
                            wifiManager.reconnect();

                            break;
                        }
                    }
                    wifiStateText.setText("Wifi is currently ON");
                    Toast.makeText(WifiActivity.this, "Wifi may take a moment to turn on", Toast.LENGTH_LONG).show();
                } else {
                    wifiManager.setWifiEnabled(false);
                    wifiStateText.setText("Wifi is currently OFF");
                }
            }
        });
    }

    public void showWifiDetails(View view) {
        TextView txtWifiDetails = (TextView)findViewById(R.id.txtWifiDetails);
        String infos = wifiManager.getConnectionInfo().toString();
        infos = infos.replaceAll(", ","\n");
       txtWifiDetails.setText(infos);
    }

    public void showOtherNetworks(View view) {
        Intent intent = new Intent(WifiActivity.this, SsidActivity.class);
        startActivity(intent);
    }

    public void reconnectWifi(View view) {
        wifiManager.setWifiEnabled(true);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
        wifiStateText.setText("Wifi is currently ON");
        Toast.makeText(WifiActivity.this, "Wifi may take a moment to turn on", Toast.LENGTH_LONG).show();
    }
}

