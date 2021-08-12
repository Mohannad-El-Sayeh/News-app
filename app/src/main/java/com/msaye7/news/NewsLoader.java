package com.msaye7.news;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/*
 * Created by Mohannad El-Sayeh on 30/07/2021
 */
public class NewsLoader extends AsyncTaskLoader<List<NewsModel>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String url;

    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<NewsModel> loadInBackground() {
        if(url == null){
            return null;
        }

        List<NewsModel> newsModels = QueryUtils.fetchNewsModelData(url);
        return newsModels;
    }
}
