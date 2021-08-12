package com.msaye7.news;

/*
 * Created by Mohannad El-Sayeh on 30/07/2021
 */
public class NewsModel {
    private String title;
    private String section;
    private String author;
    private String datePublished;
    private String webUrl;

    public NewsModel(String title, String section, String webUrl, String datePublished, String author) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.datePublished = datePublished;
        this.webUrl = webUrl;
    }

    public NewsModel(String title, String section, String webUrl, String datePublished) {
        this.title = title;
        this.section = section;
        this.datePublished = datePublished;
        this.webUrl = webUrl;
    }

    public NewsModel(String title, String section, String webUrl) {
        this.title = title;
        this.section = section;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
