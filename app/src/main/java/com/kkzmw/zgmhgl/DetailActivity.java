package com.kkzmw.zgmhgl;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.kkzmw.zgmhgl.common.BaseActivity;
import com.kkzmw.zgmhgl.common.OnlineConfigManager;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;

public class DetailActivity extends BaseActivity {

    private static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "DetailAtivity";
    private AdView bannerAD;

    private RelativeLayout mAdContent;
    private boolean mIsAddOn;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWebView = (WebView) findViewById(R.id.webview_detail);
        mIsAddOn = OnlineConfigManager.getInstance(this).getAddSwitch();
        if (mIsAddOn) {
            mAdContent = (RelativeLayout) findViewById(R.id.adcontent);
        }

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        String link = "";
        if (getIntent() != null) {
            link = getIntent().getExtras().getString(Constants.EXTRA_KEY_URL);
        }
        if(DEBUG){
            Log.d(TAG,"link:"+link);
        }
//        http://182.92.192.136/Strategy/Detail?id=5
        mWebView.loadUrl(link);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.copyBackForwardList().getCurrentIndex() > 0) {
            mWebView.goBack();
        }
        else {
            // Your exit alert code, or alternatively line below to finish
            super.onBackPressed(); // finishes activity
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mIsAddOn) {
            showBannerAdd();
        }
    }

    private void showBannerAdd() {
        this.bannerAD = new AdView(this, AdSize.BANNER, Constants.APP_ID, Constants.AD1_ID);
        this.mAdContent.removeAllViews();
        this.mAdContent.addView(bannerAD);
        bannerAD.fetchAd(new AdRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.detail, menu);
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
