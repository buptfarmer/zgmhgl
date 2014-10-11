package com.kkzmw.zgmhgl;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.kkzmw.zgmhgl.common.NotificationUtils;

import java.util.List;

public class AppsMonitorService extends Service {
    public static final String TAG = "AppsMonitor";
    public static final String TARGET_APP_STRING = "com.netease.Sanguo.Sanguo";
    public static final int EVENT_TARGET_APP_SWITCH = 1;
    private AppsMonitor mAppsMonitor;
    private Handler mHandler;
    public AppsMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mAppsMonitor = new AppsMonitor(this, mHandler);
        mAppsMonitor.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAppsMonitor.interrupt();;
    }

    static void startService(Context context){
        Intent intent = new Intent(context, AppsMonitorService.class);
        context.startService(intent);
    }
    static void stopService(Context context){
        Intent intent = new Intent(context, AppsMonitorService.class);
        context.stopService(intent);
    }
    public static class AppsMonitor extends Thread{
        private Context mAppContext;
        private ActivityManager mActivityManager;
        private boolean mStop = false;
        private boolean mLastIsTargetApp = false;
        private Handler mOutHandler;

        public AppsMonitor(Context context, Handler handler){
            mAppContext = context.getApplicationContext();
            mActivityManager = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);

            mStop = false;
            mOutHandler = handler;

        }
        public void stopMonitor(){
            mStop = true;
        }

        @Override
        public void run() {
            while(!mStop){
                ComponentName comName = getTopActivity(mActivityManager);
                if(comName == null){
                    Log.d(TAG,"comName null");
                } else {
                    Log.d(TAG,"comName:"+ comName.getClassName());
                    if(TARGET_APP_STRING.equalsIgnoreCase(comName.getClassName())){
                        mLastIsTargetApp = true;
                    } else {
                        if(mLastIsTargetApp){
                            mOutHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    targetAppSwitched();
                                }
                            });
                        }
                        mLastIsTargetApp = false;
                    }
                    Log.d(TAG,"mLastIsTargetApp:"+ mLastIsTargetApp);


                }

                try{
                    Thread.sleep(Constants.APP_MONITOR_INTERVAL);
                } catch (InterruptedException e){
                    //ignore
                }
            }
        }
        private void targetAppSwitched(){
            // TODO use notification to start @HomeActivity
            Intent intent = new Intent(mAppContext, AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Constants.EXTRA_FROM_KEY, Constants.EXTRA_FROM_VALUE_SAVEFLOW_NOTIFICATION);
            PendingIntent pi = PendingIntent.getActivity(mAppContext, R.string.title_activity_about,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String tickerText=mAppContext.getResources().getString(R.string.noti_title);
            String message = mAppContext.getResources().getString(R.string.noti_message);
            String title = tickerText;
            NotificationUtils.notify(mAppContext,title,message,
            Notification.FLAG_AUTO_CANCEL,tickerText,pi,R.string.title_activity_about);
//            Toast.makeText(mAppContext,"targetAppSwitched!",Toast.LENGTH_SHORT).show();

        }

    }
    public static ComponentName getTopActivity(ActivityManager am) {
        List<ActivityManager.RunningTaskInfo> taskList = null;
        try {
            taskList = am.getRunningTasks(1);
            if (taskList != null && taskList.size() > 0) {
                ComponentName cname = taskList.get(0).topActivity;
                return cname;
            }
        } catch (Exception e) {
            // should not be here, but in some system, the getRunningTasks will fail with crash...
        }
        return null;
    }

    public static boolean isTopActivity(String className, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName componentName = getTopActivity(am);
        if (componentName != null) {
            return componentName.getClassName().contains(className);
        }

        return false;
    }
}
