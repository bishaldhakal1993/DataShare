package com.example.datashare;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HotspotActivity extends AppCompatActivity {
    Switch switchHotspot;
    TextView hotspotStateText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot);
        switchHotspot = (Switch) findViewById(R.id.switchHotspot);
        hotspotStateText = (TextView)findViewById(R.id.hotspotStateText);
        if (ApManager.isApOn(HotspotActivity.this)) {
            Toast.makeText(HotspotActivity.this, "Hotspot is currently turned on", Toast.LENGTH_LONG).show();
            switchHotspot.setChecked(true);
            hotspotStateText.setText("Hotspot is currently switched on");
        } else {
            Toast.makeText(HotspotActivity.this, "Hotspot is currently turned off", Toast.LENGTH_LONG).show();
            switchHotspot.setChecked(false);
            hotspotStateText.setText("Hotspot is currently switched off");
        }
        switchHotspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(HotspotActivity.this, "Hotspot is currently turned on", Toast.LENGTH_LONG).show();
                    switchHotspot.setChecked(true);
                    ApManager.configApState(HotspotActivity.this);
                    hotspotStateText.setText("Hotspot is currently switched on");// change Ap state :boolean
                } else {
                    Toast.makeText(HotspotActivity.this, "Hotspot is currently turned off", Toast.LENGTH_LONG).show();
                    switchHotspot.setChecked(false);
                    ApManager.configApState(HotspotActivity.this);
                    hotspotStateText.setText("Hotspot is currently switched off");// change Ap state :boolean
                }
            }
        });

        final TrafficStats usageDetails = new TrafficStats();
        final EditText dataLimit = (EditText) findViewById(R.id.dataLimit);
        final long period = 60000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!dataLimit.getText().toString().matches("")){
                    if(Long.parseLong(dataLimit.getText().toString()) * 1024 * 1024 < usageDetails.getMobileRxBytes()){
                        if(ApManager.isApOn(HotspotActivity.this)){
                            ApManager.configApState(HotspotActivity.this);
                            switchHotspot.setChecked(false);
                            hotspotStateText.setText("Hotspot is currently switched off");
                        }
                    }
                }
            }
        }, 0, period);
    }

    public void showDevices(View view){
        BufferedReader br = null;
        String flushCmd = "sh ip -s -s neigh flush all";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(flushCmd, null, new File("/proc/net"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            TextView connectedDevices = (TextView)findViewById(R.id.txtConnectedDevices);
            String line;
            String wholeText = "\n";
            while ((line = br.readLine()) != null) {
                    wholeText = wholeText + line + "\n";
                }
            connectedDevices.setText(wholeText);
            br.close();
            } catch(Exception e) {

        }
    }
}

