package com.kkzmw.zgmhgl.common;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.kkzmw.zgmhgl.Constants;
import com.kkzmw.zgmhgl.R;
import com.testin.agent.TestinAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseActivity extends ActionBarActivity {

    private static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "BaseActivity";
    private RelativeLayout mAdContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TestinAgent.init(this, Constants.APP_KEY_TESTIN);
        mAdContent = (RelativeLayout) findViewById(R.id.adcontent);

        // for update
        UmengUpdateAgent.update(this);
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
        if(OnlineConfigManager.getInstance(this).getPushServiceSwitch()){
            PushAgent mPushAgent = PushAgent.getInstance(this);
            mPushAgent.enable();
            PushAgent.getInstance(this).onAppStart();
        }
        String device_token = UmengRegistrar.getRegistrationId(this);
        if (DEBUG) {

            Log.d("ccc", "device_token:" + device_token);
        }
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener(){
            @Override
            public void onDataReceived(JSONObject data) {
                if(data == null){
                    if(DEBUG){
                        Log.d(TAG, "online data:  null");
                    }
                } else  {
                    if(DEBUG){
                        //{"online_server_address":"182.92.192.136","add_switch":"true","push_service_switch":"true","test":"test"}
                        Log.d(TAG, "online data:"+data.toString());
                    }
                    try {
                        String address = data.getString(OnlineConfigManager.KEY_ONLINE_SERVER_ADDRESS);
                        boolean addSwitch = data.getBoolean(OnlineConfigManager.KEY_ADD_SWITH);
                        boolean pushServiceSwitch = data.getBoolean(OnlineConfigManager.KEY_PUSH_SERVICE_SWITH);
                        Log.d(TAG, "address:"+ address +"  addSwitch:"+addSwitch+"  pushServiceSwitch:"+pushServiceSwitch);
                        OnlineConfigManager configManager = OnlineConfigManager.getInstance(BaseActivity.this);
                        configManager.setOnlineServerAddress(address);
                        configManager.setKeyAddSwith(addSwitch);
                        configManager.setPushServiceSwitch(pushServiceSwitch);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String deviceInfo = CommonUtils.getDeviceInfo(this);
        TestinAgent.setUserInfo(deviceInfo);
        if (DEBUG) {
            Log.d("ccc", "onResume: " + deviceInfo);
        }
        TestinAgent.onResume(this);//此行必须放在super.onResume后

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        TestinAgent.onStop(this);//此行必须放在super.onStop后

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
