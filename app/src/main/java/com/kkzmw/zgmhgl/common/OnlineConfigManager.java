package com.kkzmw.zgmhgl.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.kkzmw.zgmhgl.Constants;

/**
 * Created by chencheng on 14-10-10.
 */
public class OnlineConfigManager {
    private static OnlineConfigManager ourInstance;

    private static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "OnlineConfigManager";
    private static final String ONLINE_CONFIG = "online_config";
    public static final String KEY_ALARM_MONTH_BEYOND_TYPE = "alarm_month_beyond_type";
    public static final String KEY_ONLINE_SERVER_ADDRESS = "online_server_address";
    public static final String KEY_ADD_SWITH = "add_switch";
    public static final String KEY_PUSH_SERVICE_SWITH = "push_service_switch";
    public static final String KEY_POP_UP_MONITOR_SWITH = "pop_up_monitor_switch";


    private SharedPreferences mPref;

    public static OnlineConfigManager getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new OnlineConfigManager(context.getApplicationContext());
        }
        return ourInstance;
    }

    private OnlineConfigManager(Context context) {
        mPref = context.getSharedPreferences(ONLINE_CONFIG, Context.MODE_PRIVATE);
    }
    public void onAppStart(){
        getOnlineData();
    }
    public void getOnlineData(){

    }

    public int getAlarmMonthBeyondType() {
        return mPref.getInt(KEY_ALARM_MONTH_BEYOND_TYPE, 0);
    }

    public void setAlarmMonthBeyondType(int alarmMonthBeyondType) {
        SharedPreferencesCompat.apply(mPref.edit().putInt(KEY_ALARM_MONTH_BEYOND_TYPE, alarmMonthBeyondType));

    }

    public boolean getAddSwitch(){
        return mPref.getBoolean(KEY_ADD_SWITH, Constants.DEFAULT_ADD_SWITCH);
    }
    public void setKeyAddSwith(boolean switchValue){
        SharedPreferencesCompat.apply(mPref.edit().putBoolean(KEY_ADD_SWITH, switchValue));
    }
    public String getOnlineServerAddress(){
        return mPref.getString(KEY_ONLINE_SERVER_ADDRESS, Constants.DEFAULT_SERVER_ADDRESS);
    }
    public void setOnlineServerAddress(String address){
        SharedPreferencesCompat.apply(mPref.edit().putString(KEY_ONLINE_SERVER_ADDRESS, address));

    }
    public boolean getPushServiceSwitch(){
        return mPref.getBoolean(KEY_PUSH_SERVICE_SWITH,Constants.DEFAULT_PUSH_SERVICE_SWITCH);
    }
    public void setPushServiceSwitch(boolean enable){
        SharedPreferencesCompat.apply(mPref.edit().putBoolean(KEY_PUSH_SERVICE_SWITH, enable));

    }
    public boolean getPopUpMonitorSwitch(){
        return mPref.getBoolean(KEY_POP_UP_MONITOR_SWITH,true);
    }
    public void setPopUpMonitorSwitch(boolean enable){
        SharedPreferencesCompat.apply(mPref.edit().putBoolean(KEY_POP_UP_MONITOR_SWITH, enable));

    }
}
