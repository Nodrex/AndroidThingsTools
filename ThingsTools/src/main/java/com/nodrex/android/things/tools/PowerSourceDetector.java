package com.nodrex.android.things.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.TextView;

/**
 * Created by nchum on 1/29/2017.
 */
public class PowerSourceDetector extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        TextView power = null; //ThingsView.getpowerTextView();
        if(power == null) return;
        switch (plugged){
            case BatteryManager.BATTERY_PLUGGED_AC:
                power.setText("power: AC");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                power.setText("power: usb");
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                power.setText("power: wireless");
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                power.setText("power: discharging");
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                power.setText("battery health cold");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                power.setText("battery health over voltage");
                break;
        }
    }
}
