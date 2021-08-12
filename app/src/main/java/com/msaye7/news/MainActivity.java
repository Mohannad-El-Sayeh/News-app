package com.msaye7.news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsModel>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String REQUEST_URL = "https://content.guardianapis.com/search";

    private static final int LOADER_ID = 1;

    private static final String API_KEY = "37a68596-57f9-409e-80e6-cdd39c00cc43";


    NewsAdapter adapter;


    @BindView(R.id.rv_main) RecyclerView mainRV;
    @BindView(R.id.loading_indicator) ProgressBar loadingIndicator;
    @BindView(R.id.no_data_found) TextView noDataTV;
    @BindView(R.id.no_internet) TextView noInternetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapter = new NewsAdapter(this, new ArrayList<>());

        mainRV.setAdapter(adapter);
        mainRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        startConnection();
    }

    private void startConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();

            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            noInternetTV.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public Loader<List<NewsModel>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsModel>> loader, List<NewsModel> newsModels) {
        loadingIndicator.setVisibility(View.GONE);
        if(newsModels != null && !newsModels.isEmpty()){
            adapter.newsModels.addAll(newsModels);
            adapter.notifyDataSetChanged();
        }else {
            noDataTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsModel>> loader) {
        adapter.newsModels.clear();
        adapter.notifyDataSetChanged();
    }
}