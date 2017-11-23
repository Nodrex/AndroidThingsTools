package com.nodrex.android.things.tools;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Thing;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.nodrex.android.tools.Util;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;

/**
 * Created by nchum on 1/28/2017.
 */
public class ThingsUtil {

    private static PeripheralManagerService peripheralManagerService;
    private static final String TAG = "AndroidThingsLogTag";
    private static String ip;

    public static void detectGlobalIp(final TextView textView){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                ip = Util.getGlobalIpAddress();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(textView != null) textView.setText(ip);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void log(String text){
        Log.d(TAG, text);
        Util.log(text);
        TextView debug = ThingsView.getDebug();
        if(debug == null)return;
        int line = debug.getLineCount();
        if(line <= 0 || line > 200) debug.setText(text);
        else {
            debug.append("\n"+text);
            ScrollView sc = ThingsView.getScrollView();
            if(sc != null) sc.fullScroll(View.FOCUS_DOWN);
        }
    }

    /**
     * @return PeripheralManagerService for future use.
     */
    public static PeripheralManagerService getPeripheralManagerService() {
        if (peripheralManagerService != null) return peripheralManagerService;
        peripheralManagerService =  new PeripheralManagerService();
        return peripheralManagerService;
    }

    public static Gpio openGpioAsOut(String gpio) throws IOException {
        return openGpioAsOut(gpio,false);
    }

    public static Gpio openGpioAsOut(String gpio, boolean initialHigh) throws IOException {
        PeripheralManagerService service = getPeripheralManagerService();
        if(service == null) return null;
        Gpio gpioPin = service.openGpio(gpio);
        gpioPin.setDirection(initialHigh ? Gpio.DIRECTION_OUT_INITIALLY_HIGH : Gpio.DIRECTION_OUT_INITIALLY_LOW);
        return gpioPin;
    }

    public static Gpio openGpioAsIn(String gpio) throws IOException {
        PeripheralManagerService service = getPeripheralManagerService();
        if(service == null) return null;
        Gpio gpioPin = service.openGpio(gpio);
        gpioPin.setDirection(Gpio.DIRECTION_IN);
        return gpioPin;
    }

    public static void close(Gpio ... gpios){
        if(gpios == null) return;
        for (Gpio gpi: gpios) {
            if(gpi == null) continue;
            try {
                gpi.close();
            } catch (IOException e) {
                log("There was some problem, while trying to close gpio: " + e.toString());
            }
        }
    }




    public static void restartDevice(){
        try {
            getRuntime().exec("sudo reboot");
        } catch (Exception e) {
            log("There was some problem while trying to restart (execute executing getRuntime().exec(\"sudo reboot\")) device: " + e.toString());
        }
    }

    public static void test(Activity activity){
        try {
            /*PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            pm.reboot(null);*/
            //com.google.android.things.devicemanagement.DeviceManager.reboot();
        } catch (Exception e) {
            log("There was some problem while trying to restart (PowerManager reboot) device: " + e.toString());
        }
    }




}
