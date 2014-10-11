package com.zhy.csdn;

import android.content.Context;

import com.kkzmw.zgmhgl.KkzmwApplication;
import com.kkzmw.zgmhgl.common.OnlineConfigManager;

public class URLUtil {

    public static final String NEWS_LIST_URL = "http://www.csdn.net/headlines.html";
    public static final String NEWS_LIST_URL_YIDONG = "http://mobile.csdn.net/mobile";
    public static final String NEWS_LIST_URL_YANFA = "http://sd.csdn.net/sd";
    public static final String NEWS_LIST_URL_YUNJISUAN = "http://cloud.csdn.net/cloud";
    public static final String NEWS_LIST_URL_ZAZHI = "http://programmer.csdn.net/programmer";
    public static final String NEWS_LIST_URL_YEJIE = "http://news.csdn.net/news";

    public static final String NEWS_LIST_URL_ZGMH = "http://news.4399.com/gonglue/zgmh/xinde";
    public static final String NEWS_LIST_URL_ZGMH_BASE = "http://news.4399.com";


    public static String generateDetailUrl(Context context,int type, int id){
        String urlStr = "";
        switch (type){
            case Constaint.NEWS_TYPE_ZGMH:
                urlStr = "http://" + OnlineConfigManager.getInstance(context).getOnlineServerAddress() + "/Strategy/Detail?id="+id;
                break;
            default:
                break;
        }
        return urlStr;
    }

    public static String generateUrl(Context context,int newsType, int currentPage) {
        currentPage = currentPage > 0 ? currentPage : 1;
        String urlStr = "";
        switch (newsType) {
            case Constaint.NEWS_TYPE_ZGMH:
                urlStr = "http://" + OnlineConfigManager.getInstance(context).getOnlineServerAddress() + "/Strategy/Data?page=" + currentPage + "&size=10";
                break;
            default:
                urlStr = NEWS_LIST_URL_YIDONG;
                urlStr += "/" + currentPage;
                break;
        }
        return urlStr;
    }
    /**
     * @param newsType
     * @param currentPage
     * @return
     */
    public static String generateUrl(int newsType, int currentPage) {
        currentPage = currentPage > 0 ? currentPage : 1;
        String urlStr = "";
        switch (newsType) {
            case Constaint.NEWS_TYPE_YEJIE:
                urlStr = NEWS_LIST_URL_YEJIE;
                urlStr += "/" + currentPage;
                break;
            case Constaint.NEWS_TYPE_YANFA:
                urlStr = NEWS_LIST_URL_YANFA;
                urlStr += "/" + currentPage;
                break;
            case Constaint.NEWS_TYPE_CHENGXUYUAN:
                urlStr = NEWS_LIST_URL_ZAZHI;
                urlStr += "/" + currentPage;
                break;
            case Constaint.NEWS_TYPE_YUNJISUAN:
                urlStr = NEWS_LIST_URL_YUNJISUAN;
                urlStr += "/" + currentPage;
                break;
            case Constaint.NEWS_TYPE_ZGMH:
                urlStr = "http://" + OnlineConfigManager.getInstance(KkzmwApplication.getContext()).getOnlineServerAddress() + "/Strategy/Data?page=" + currentPage + "&size=10";
                break;
            default:
                urlStr = NEWS_LIST_URL_YIDONG;
                urlStr += "/" + currentPage;
                break;
        }


        return urlStr;

    }

    public static String getBaseUrl(int newsType) {
        String urlStr = "";
        switch (newsType) {
//		case Constaint.NEWS_TYPE_YEJIE:
//		case Constaint.NEWS_TYPE_YANFA:
//		case Constaint.NEWS_TYPE_CHENGXUYUAN:
//		case Constaint.NEWS_TYPE_YUNJISUAN:
            case Constaint.NEWS_TYPE_ZGMH:
                urlStr = NEWS_LIST_URL_ZGMH_BASE;
                break;
            default:
                urlStr = NEWS_LIST_URL_ZGMH_BASE;
                break;
        }


        return urlStr;
    }
}
