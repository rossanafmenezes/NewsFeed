package com.example.newsfeed;

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

public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<News> fetchNewsData (String requestUrl){

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "issue with the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News> newsList = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return newsList;
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

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the guardian news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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

    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)){

            return null;
    }


        List<News> news = new ArrayList<>();

        try {

            JSONObject newsJsonResponse = new JSONObject(newsJSON);

            JSONObject response = newsJsonResponse.getJSONObject("response");

            JSONArray newsArray = response.getJSONArray("results");

            for (int i = 0; i< newsArray.length(); i++){

                JSONObject currentNews = newsArray.getJSONObject(i);

                String section = currentNews.getString("sectionName");

                String headline = currentNews.getString("webTitle");

                String date = currentNews.getString("webPublicationDate ");

                String url = currentNews.getString("webUrl");

                StringBuilder author = new StringBuilder("By: ");

                JSONArray authorArray = currentNews.getJSONArray("tags");

                if (authorArray != null && authorArray.length() >0) {

                    for (int j = 0; j < authorArray.length(); j++){

                    JSONObject authors = authorArray.getJSONObject(j);

                    String authorListed = authors.optString("webTitle");

                    if (authorArray.length() >1){
                        author.append(authorListed);
                        author.append("\t\t\t");

                    } else {

                        author.append(authorListed);
                    }
                }

            } else {

                    author.replace(0, 3, "The Guardian");
                }

                News newsArticles = new News (headline, author.toString(), date, section, url);

                news.add(newsArticles);
        }


    } catch (JSONException e){

            Log.e("QueryUtils", "Something wrong. Problem parsing the Json results", e);
        }

        return news;
    }
}
