package com.zhy.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItem {
    private int id;

    private String title;
    private String link;
    private String date;
    /**
     */
    private String imgLink;
    /**
     */
    private String content;

    /**
     */
    private int newsType;
    private String briefContent;
    private long publicTime;
    private long createTime;


    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsItem [id=" + id + ", title=" + title + ", date=" + getDate() +
                ", newsType=" + newsType + ", content=" + content + ", link=" + link + ", imgLink=" + imgLink + "]";
    }

    public String getBriefContent() {
        return briefContent;
    }

    public void setBriefContent(String briefContent) {
        this.briefContent = briefContent;
    }

    public String getPublicTimeString() {
        return getDate();
    }

    public long getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(long publicTime) {
        this.publicTime = publicTime;
        setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(publicTime)));
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
