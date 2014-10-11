package com.kkzmw.zgmhgl;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.kkzmw.zgmhgl.common.OnlineConfigManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chencheng on 14-10-10.
 */
public class KkzmwApplication extends Application {
    private static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "KkzmwApplication";
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        Log.i(TAG, "OptimizerApp onCreate()："
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA).format(new Date()));
        long startTime = System.currentTimeMillis();

        // workaround for AsyncTask inner Handler issue
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "should never happen", e);
        }

        onAppStart();
    }

    private void onAppStart() {

        //get online config
        OnlineConfigManager.getInstance(getApplicationContext()).onAppStart();

        // start monitor service
        if (OnlineConfigManager.getInstance(getApplicationContext()).getPopUpMonitorSwitch()) {
            AppsMonitorService.startService(getApplicationContext());
        }
    }
    public static Context getContext(){
        return sInstance.getApplicationContext();
    }
}
