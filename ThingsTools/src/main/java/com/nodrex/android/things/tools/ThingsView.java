package com.nodrex.android.things.tools;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nodrex.android.tools.Util;

/**
 * Created by nchum on 1/29/2017.
 */
public class ThingsView implements View.OnClickListener{

    private static ThingsView thingsView;
    private static TextView debug;
    private static ScrollView scrollView;
    private Dialog hardwareInfo;
    private Activity activity;

    public static ThingsView getInstance(Activity activity){
        if(thingsView != null) return thingsView;
        return new ThingsView(activity);
    }

    private ThingsView(Activity activity){
        this.activity = activity;
        initLayout(activity);
    }

    private void initLayout(Activity activity){
        View v = activity.getLayoutInflater().inflate(R.layout.things_status,null);
        Window window = activity.getWindow();
        if(window == null)return;
        View decor = window.getDecorView();
        if(decor == null) return;
        View rootView = decor.getRootView();
        if(rootView == null) return;
        if(rootView instanceof LinearLayout){
            LinearLayout layout = (LinearLayout) rootView;
            layout.addView(v,0);
        }else if(rootView instanceof ScrollView){
            ScrollView layout = (ScrollView) rootView;
            layout.addView(v,0);
        }
        initViews(activity);
    }

    private void initViews(Activity activity) throws NullPointerException{
        View v =  activity.findViewById(R.id.hardwareInfo);
        if(v != null) v.setOnClickListener(this);
        v = activity.findViewById(R.id.closeApp);
        if(v != null)v.setOnClickListener(this);
        v = activity.findViewById(R.id.restartApp);
        if(v != null) v.setOnClickListener(this);
        v = activity.findViewById(R.id.restartDevice);
        if(v != null) v.setOnClickListener(this);
        debug = (TextView) activity.findViewById(R.id.debug);
        debug.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                debug.setText("");
                return false;
            }
        });
        scrollView = (ScrollView) activity.findViewById(R.id.scrollView);
        initConnection(activity);
    }

    private void initConnection(Activity activity){
        TextView rpi3ip = (TextView) activity.findViewById(R.id.rpi3Ip);
        if(Util.isNetworkConnected(activity)){
            int conType = Util.getConnectionType(activity);
            ImageView imageView = (ImageView) activity.findViewById(R.id.network);
            switch (conType){
                case Util.ETHERNET:
                    imageView.setImageResource(R.drawable.ethernet);
                    break;
                case Util.WIFI:
                    imageView.setImageResource(R.drawable.wifi);
                    break;
            }
            rpi3ip.append(Util.getLocalIpAddress());
        }else rpi3ip.setText("There is no network connection");
    }

    public void onStop(){
        hardwareInfo = Util.clearDialog(hardwareInfo);
    }

    public void onDestroy(){
        hardwareInfo = Util.clearDialog(hardwareInfo);
        debug = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.closeApp){
            Util.closeApp(activity);
        }else if (id == R.id.restartApp){
            Util.restartApp(activity,activity.getClass(),1);
        }else if(id == R.id.restartDevice){
            //ThingsUtil.restartDevice();
            //ThingsUtil.test(this);
            ThingsUtil.log("Does not works yet");
        } else {
            hardwareInfo = Util.create(activity,false,true,true,R.layout.device_hardware_information);
            TextView hardware = (TextView) hardwareInfo.findViewById(R.id.hardware);
            hardware.setText("");
            String data = Util.getDeviceName();
            hardware.append("Name: " + data + "\n");
            data = Util.getDeviceId(activity);
            hardware.append("Id: " + data + "\n");
            data = Util.getChipset();
            hardware.append("CPU: " + data + "\n");
            data = Util.getMacAddress(activity);
            hardware.append("Mac: " + data + "\n");
            data = Util.getOSVersion();
            hardware.append("OS version: " + data + "\n");
            data = Util.getResolution(activity);
            hardware.append("Resolution: " + data + "\n");
            data = System.getProperty("os.arch");
            hardware.append("CPU architecture: " + data + "\n");
            data = Build.BOOTLOADER;
            hardware.append("Boot loader version: " + data + "\n");
            data = Build.BRAND;
            hardware.append("Brand: " + data + "\n");
            hardwareInfo.show();
        }
    }

    public static TextView getDebug() {
        return debug;
    }

    public static ScrollView getScrollView() {
        return scrollView;
    }

}
