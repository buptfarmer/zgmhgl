package com.kkzmw.zgmhgl;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkzmw.zgmhgl.common.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.bean.CommonException;
import com.zhy.bean.NewsItem;
import com.zhy.biz.Items4399Biz;
import com.zhy.csdn.Constaint;

import java.util.ArrayList;
import java.util.List;

import com.kkzmw.zgmhgl.dao.NewsItemDao;
import com.kkzmw.zgmhgl.utils.AppUtil;
import com.kkzmw.zgmhgl.utils.NetUtil;
import com.kkzmw.zgmhgl.utils.ToastUtil;
import com.zhy.csdn.DataUtil;
import com.zhy.csdn.URLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

public class NewsListActivity extends BaseActivity implements IXListViewRefreshListener, IXListViewLoadMore {

    private static final int LOAD_MORE = 0x110;
    private static final int LOAD_REFREASH = 0x111;

    private static final int TIP_ERROR_NO_NETWORK = 0X112;
    private static final int TIP_ERROR_SERVER = 0X113;
    private static final boolean DEBUG = Constants.DEBUG;
    private static final String TAG = "NewsListActivity";
    private XListView mXListView;
    private NewsItemAdapter mAdapter;
    private Items4399Biz mNewsItemBiz;
    private List<NewsItem> mDatas = new ArrayList<NewsItem>();
    private NewsItemDao mNewsItemDao;
    /**
     * 是否是第一次进入
     */
    private boolean isFirstIn = true;

    /**
     * 是否连接网络
     */
    private boolean isConnNet = false;

    /**
     * 当前数据是否是从网络中获取的
     */
    private boolean isLoadingDataFromNetWork;

    /**
     * 默认的newType
     */
    private int mNewsType = Constaint.NEWS_TYPE_ZGMH;
    /**
     * Í
     * 当前页面
     */
    private int mCurrentPage = 1;

    //    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        mNewsItemBiz = new Items4399Biz();
        mXListView = (XListView) findViewById(R.id.list);
        mAdapter = new NewsItemAdapter(this, mDatas);
        mXListView.setAdapter(mAdapter);
        mXListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsItem clickedItem = mDatas.get(i);
                Intent intent = new Intent(NewsListActivity.this,DetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_URL,clickedItem.getLink());
                startActivity(intent);
            }
        });
        mXListView.setPullRefreshEnable(this);
        mXListView.setPullLoadEnable(this);
        mXListView.setRefreshTime(AppUtil.getRefreashTime(this, Constaint.NEWS_TYPE_ZGMH));
        mNewsItemDao = new NewsItemDao(this);

        isFirstIn = true;

        AppsMonitorService.startService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstIn) {
            /**
             * 进来时直接刷新
             */
            mXListView.startRefresh();
            isFirstIn = false;
        } else {
            mXListView.NotRefreshAtBegin();
        }
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
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new LoadDatasTask().execute(LOAD_REFREASH);

    }

    @Override
    public void onLoadMore() {
        new LoadDatasTask().execute(LOAD_MORE);

    }

    class LoadDatasTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            switch (params[0]) {
                case LOAD_MORE:
                    loadMoreData();
                    break;
                case LOAD_REFREASH:
                    return refreashData();
            }
            return -1;

        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case TIP_ERROR_NO_NETWORK:
                    ToastUtil.toast(NewsListActivity.this, "没有网络连接！");
                    mAdapter.setDatas(mDatas);
                    mAdapter.notifyDataSetChanged();
                    break;
                case TIP_ERROR_SERVER:
                    ToastUtil.toast(NewsListActivity.this, "服务器错误！");
                    break;

                default:
                    break;

            }

            mXListView.setRefreshTime(AppUtil.getRefreashTime(NewsListActivity.this, mNewsType));
            mXListView.stopRefresh();
            mXListView.stopLoadMore();
//            mAdapter.addAll(mDatas);
//            mAdapter.notifyDataSetChanged();
//            mXListView.stopRefresh();
        }


    }

    /**
     * 下拉刷新数据
     */
    public Integer refreashData() {

        if (NetUtil.checkNet(NewsListActivity.this)) {
            isConnNet = true;
            // 获取最新数据
            try {
                List<NewsItem> newsItems = getNewsItems(mNewsType, mCurrentPage);
                mAdapter.setDatas(newsItems);

                isLoadingDataFromNetWork = true;
                // 设置刷新时间
                AppUtil.setRefreashTime(NewsListActivity.this, mNewsType);
                // 清除数据库数据
                mNewsItemDao.deleteAll(mNewsType);
                // 存入数据库
                mNewsItemDao.add(newsItems);

            } catch (CommonException e) {
                e.printStackTrace();
                isLoadingDataFromNetWork = false;
                return TIP_ERROR_SERVER;
            }
        } else {
            isConnNet = false;
            isLoadingDataFromNetWork = false;
            // TODO从数据库中加载
            List<NewsItem> newsItems = mNewsItemDao.list(mNewsType, mCurrentPage);
            mDatas = newsItems;
            //mAdapter.setDatas(newsItems);
            return TIP_ERROR_NO_NETWORK;
        }

        return -1;

    }

    public List<NewsItem> getNewsItems(int type, int page) throws CommonException {

        String urlStr = URLUtil.generateUrl(this, type, page);


        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        NewsItem newsItem = null;
        try {
            String jsonStr = DataUtil.doGet(urlStr);
            JSONArray jsonArray = new JSONArray(jsonStr);
            int length = jsonArray.length();
            for (int index = 0; index < length; index++) {
                JSONObject item = jsonArray.getJSONObject(index);
                String title = item.getString("Topic");
                String imgLink = item.getString("TopicCover");
//                String createTime = item.getString("CreatedTime");
                String breifContent = item.getString("ContentShortcut");
                int id = item.getInt("Id");
                long publicTime = item.getLong("PublicTime");

                if (DEBUG) {
                    Log.d(TAG, "json: title:" + title + "  imgLink:" + imgLink + "  publicTime:" + publicTime + "  breifContent:" + breifContent);
                }
                newsItem = new NewsItem();
                newsItem.setTitle(title);
                newsItem.setImgLink(imgLink);
                newsItem.setBriefContent(breifContent);
                newsItem.setId(id);
                newsItem.setPublicTime(publicTime);

                newsItem.setLink(URLUtil.generateDetailUrl(this,type,id));
                newsItem.setNewsType(type);
                newsItems.add(newsItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItems;
    }

    /**
     * 会根据当前网络情况，判断是从数据库加载还是从网络继续获取
     */
    public void loadMoreData() {
        // 当前数据是从网络获取的
        if (isLoadingDataFromNetWork) {
            mCurrentPage += 1;
            try {
                List<NewsItem> newsItems = getNewsItems(mNewsType, mCurrentPage);
                mNewsItemDao.add(newsItems);
                mAdapter.addAll(newsItems);
                mAdapter.notifyDataSetChanged();
            } catch (CommonException e) {
                e.printStackTrace();
            }
        } else {
            // 从数据库加载的
            mCurrentPage += 1;
            List<NewsItem> newsItems = mNewsItemDao.list(mNewsType, mCurrentPage);
            mAdapter.addAll(newsItems);
            mAdapter.notifyDataSetChanged();

        }

    }

    public static class NewsItemAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<NewsItem> mDatas;

        /**
         * 使用了github开源的ImageLoad进行了数据加载
         */
        private ImageLoader imageLoader = ImageLoader.getInstance();
        private DisplayImageOptions options;

        public NewsItemAdapter(Context context, List<NewsItem> datas) {
            this.mDatas = datas;
            mInflater = LayoutInflater.from(context);

            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon)
                    .showImageForEmptyUri(R.drawable.icon).showImageOnFail(R.drawable.icon).cacheInMemory()
                    .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

        public void addAll(List<NewsItem> mDatas) {
            this.mDatas.addAll(mDatas);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        public void setDatas(List<NewsItem> mDatas) {
            this.mDatas.clear();
            this.mDatas.addAll(mDatas);
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.news_item_layout, null);
                holder = new ViewHolder();

                holder.mContent = (TextView) convertView.findViewById(R.id.id_content);
                holder.mTitle = (TextView) convertView.findViewById(R.id.id_title);
                holder.mDate = (TextView) convertView.findViewById(R.id.id_date);
                holder.mImg = (ImageView) convertView.findViewById(R.id.id_newsImg);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsItem newsItem = mDatas.get(position);
            holder.mTitle.setText(newsItem.getTitle());
            holder.mContent.setText(newsItem.getBriefContent());
            holder.mDate.setText(newsItem.getDate());
            if (newsItem.getImgLink() != null) {
                holder.mImg.setVisibility(View.VISIBLE);
                imageLoader.displayImage(newsItem.getImgLink(), holder.mImg, options);
            } else {
                holder.mImg.setVisibility(View.GONE);
            }

            return convertView;
        }

        private final class ViewHolder {
            TextView mTitle;
            TextView mContent;
            ImageView mImg;
            TextView mDate;
        }

    }
}
