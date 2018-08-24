package com.example.android.newsapp;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    /** Tag for log messages */
    private static final String LOG_TAG = StoryLoader.class.getName();

    /** Query URL */
    private String mUrl;


    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.d(LOG_TAG, "loader start");
    }

    @Override
    public List<Story> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            Log.d(LOG_TAG, "Trouble with url");
            return null;
        }

        // Perform the HTTP request for earthquake data and process the response.
        Log.d(LOG_TAG, "Loader extract Stories");
        return Utils.extractStories(mUrl);
    }
}
