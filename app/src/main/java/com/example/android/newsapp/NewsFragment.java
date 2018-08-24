package com.example.android.newsapp;


import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Story>>{

    private static final String LOG_TAG = MainActivity.class.getName();


    /** URL for earthquake data from the GUARDIAN dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q&format=json&section=uk-news&from-date=2018-08-08&&page-size=50&show-tags=contributor&order-by=newest&api-key=67356727-726a-44c1-aedb-5b9b0c272df9";

    // Create a new {@link ArrayAdapter} of earthquakes
    private StoryAdapter storyAdapter;


    /**
     * Constant value for the story loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int STORY_LOADER_ID = 1;

    private TextView emptyView;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_list, container, false);
        storyAdapter = new StoryAdapter(getActivity(),new LinkedList<Story>());

        // Find a reference to the {@link ListView} in the layout
        ListView storyListView = rootView.findViewById(R.id.list);

        TextView emptyView = rootView.findViewById(R.id.empty_view);

        storyListView.setEmptyView(emptyView);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        storyListView.setAdapter(storyAdapter);

        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = storyAdapter.getItem(position);

                Uri earthquakeUri = null;
                if (story != null) {
                    earthquakeUri = Uri.parse(story.getUrl());
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        // If there is a network connection, fetch data
        if (isNetworkAvailable(getActivity())) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            getLoaderManager().initLoader(STORY_LOADER_ID, null, this);
            Log.d(LOG_TAG, "loader init");

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyView.setText(R.string.empty_view);
        }
        return rootView;
    }
    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "loader create");
        return new StoryLoader(getActivity(), GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {

        TextView emptyView = getView().findViewById(R.id.empty_view);

       // Hide loading indicator because the data has been loaded
        View loadingIndicator = getView().findViewById(R.id.loading_indicator);

        if (isNetworkAvailable(getActivity())) {

            emptyView.setText(R.string.no_internet);

        } else {
            // Set empty state text to display "No earthquakes found."
            emptyView.setText(R.string.empty_view);

        }

        loadingIndicator.setVisibility(View.GONE);


        // Clear the adapter of previous stories data
        storyAdapter.clear();

        // If there is a valid list of {@link Story}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (stories != null && !stories.isEmpty()) {
            storyAdapter.addAll(stories);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        // Clear the adapter of previous stories data
        storyAdapter.clear();
        Log.d(LOG_TAG, "loader reset");

    }

    public static boolean isNetworkAvailable(Context context) {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
