package com.kkzmw.zgmhgl;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.kkzmw.zgmhgl.common.BaseActivity;
import com.kkzmw.zgmhgl.common.OnlineConfigManager;
import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.umeng.analytics.MobclickAgent;

import com.kkzmw.zgmhgl.R;
import com.umeng.fb.FeedbackAgent;

public class AboutActivity extends BaseActivity {

    private Button mFeedBack;
    private CheckBox mPushService;
    private CheckBox mPopUpMonitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mFeedBack = (Button) findViewById(R.id.action_feedback);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
                agent.startFeedbackActivity();
            }
        });
        mPushService = (CheckBox) findViewById(R.id.push_service);
        mPushService.setChecked(OnlineConfigManager.getInstance(AboutActivity.this).getPushServiceSwitch());
        mPushService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                OnlineConfigManager.getInstance(AboutActivity.this).setPushServiceSwitch(b);
            }
        });
        mPopUpMonitor = (CheckBox) findViewById(R.id.pop_up);
        mPopUpMonitor.setChecked(OnlineConfigManager.getInstance(AboutActivity.this).getPopUpMonitorSwitch());
        mPopUpMonitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                OnlineConfigManager.getInstance(AboutActivity.this).setPopUpMonitorSwitch(b);
                if(b){
                    // start monitor service
                    AppsMonitorService.startService(getApplicationContext());
                } else {
                    // stop monitor service
                    AppsMonitorService.stopService(getApplicationContext());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
