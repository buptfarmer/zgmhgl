package com.zhy.biz;

import com.zhy.bean.CommonException;
import com.zhy.bean.NewsItem;
import com.zhy.csdn.Constaint;
import com.zhy.csdn.DataUtil;
import com.zhy.csdn.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhy
 * 
 */
public class Items4399Biz {
	/**
	 *
	 * @param htmlStr
	 * @return
	 * @throws CommonException
	 */
	public List<NewsItem> getNewsItems(int newsType, int currentPage)
			throws CommonException {
		String urlStr = URLUtil.generateUrl(newsType, currentPage);

		String htmlStr = DataUtil.doGet(urlStr);

		List<NewsItem> newsItems = new ArrayList<NewsItem>();
		NewsItem newsItem = null;

		Document doc = Jsoup.parse(htmlStr);
		doc.getElementsByTag("li");
		Elements listText = doc.getElementsByClass("listTxt");
		log("listText.size():"+listText.size());
		Elements listItems = listText.get(0).getElementsByTag("li");
		log("listItems.size():"+listItems.size());

		for(int i =0; i< listItems.size(); i++){
			Element item = listItems.get(i);
			String title = item.getElementsByClass("bigtitle").text();
			String href = URLUtil.getBaseUrl(Constaint.NEWS_TYPE_ZGMH)+item.getElementsByClass("bigtitle").get(0).attr("href");
			String date = item.child(0).child(1).text();
			String imgSrc = item.getElementsByTag("img").get(0).attr("src");
			String intro = item.getElementsByClass("wz_info").get(0).text();

			newsItem = new NewsItem();
			newsItem.setNewsType(Constaint.NEWS_TYPE_ZGMH);
			newsItem.setTitle(title);
			newsItem.setLink(href);
			newsItem.setImgLink(imgSrc);
			newsItem.setContent(intro);
			// add to list
			newsItems.add(newsItem);
		}

		return newsItems;

	}

	public static void main(String[] args) {
		String urlStr = URLUtil.generateUrl(Constaint.NEWS_TYPE_ZGMH, 1);
		try {
			String htmlStr = DataUtil.doGet(urlStr);

			Document doc = Jsoup.parse(htmlStr);

			doc.getElementsByTag("li");
			Elements listText = doc.getElementsByClass("listTxt");
			log("listText.size():"+listText.size());
			Elements listItems = listText.get(0).getElementsByTag("li");
			log("listItems.size():"+listItems.size());

			List<NewsItem> newsItems = new ArrayList<NewsItem>();
			for(int i =0; i< listItems.size(); i++){
				Element item = listItems.get(i);
				String title = item.getElementsByClass("bigtitle").text();
				String href = URLUtil.getBaseUrl(Constaint.NEWS_TYPE_ZGMH)+item.getElementsByClass("bigtitle").get(0).attr("href");
				String date = item.child(0).child(1).text();
				String imgSrc = item.getElementsByTag("img").get(0).attr("src");
				String intro = item.getElementsByClass("wz_info").get(0).text();

				NewsItem newsItem = new NewsItem();
				newsItem.setNewsType(Constaint.NEWS_TYPE_ZGMH);
				newsItem.setTitle(title);
				newsItem.setLink(href);
				newsItem.setImgLink(imgSrc);
				newsItem.setContent(intro);
				// add to list
				newsItems.add(newsItem);
			}
			for(NewsItem item : newsItems){
				log(item.toString());
			}
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void log(String msg){
		System.out.println(msg);
	}
}
