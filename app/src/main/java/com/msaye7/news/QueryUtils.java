package com.msaye7.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Mohannad El-Sayeh on 30/07/2021
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<NewsModel> fetchNewsModelData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<NewsModel> newsModels = extractFeatureFromJson(jsonResponse);

        return newsModels;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the NewsModel JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsModel> extractFeatureFromJson(String newsModelJSON) {
        if (TextUtils.isEmpty(newsModelJSON)) {
            return null;
        }

        List<NewsModel> newsModels = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsModelJSON);

            JSONObject response = baseJsonResponse.getJSONObject("response");

            JSONArray newsModelArray = response.getJSONArray("results");

            for (int i = 0; i < newsModelArray.length(); i++) {

                JSONObject currentNewsModel = newsModelArray.getJSONObject(i);

                String title = currentNewsModel.getString("webTitle");

                String section = currentNewsModel.getString("sectionName");

                String webUrl = currentNewsModel.getString("webUrl");

                String datePublished = currentNewsModel.optString("webPublicationDate");

                JSONArray tagsArray = currentNewsModel.optJSONArray("tags");

                String author = null;
                if (tagsArray != null) {
                    author = tagsArray.getJSONObject(0).getString("webTitle");
                }


                NewsModel newsModel;
                if(TextUtils.isEmpty(datePublished) && TextUtils.isEmpty(author)){
                    newsModel = new NewsModel(title, section, webUrl);
                }else if(TextUtils.isEmpty(author)){
                    newsModel = new NewsModel(title, section, webUrl, datePublished);
                }else{
                    newsModel = new NewsModel(title, section, webUrl, datePublished, author);
                }

                newsModels.add(newsModel);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the NewsModel JSON results", e);
        }

        return newsModels;
    }

}
