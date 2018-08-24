package com.example.android.newsapp;

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
import java.util.LinkedList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class Utils {

    /** Tag for the log messages */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link Utils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Utils() {
    }

    /**
     * Return a list of {@link Story} objects that has been built up from
     * parsing a JSON response.
     */
    public static LinkedList<Story> extractStories(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object

        // Return the {@link Event}

        Log.d(LOG_TAG, "Query extract Earthquakes successfully");
        return extractFeatureFromJson(jsonResponse);

    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    /**
     * Return an {@link Story} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */

    private static LinkedList<Story> extractFeatureFromJson (String storyJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        LinkedList<Story> stories = new LinkedList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject root = new JSONObject(storyJSON);
            JSONObject response = root.getJSONObject("response");

            JSONArray resultsArray = response.getJSONArray("results");

            // build up a list of Earthquake objects with the corresponding data.

            int length = resultsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject item = resultsArray.getJSONObject(i);

                JSONArray tags = item.getJSONArray("tags");

                if (!tags.isNull(0) ) {
                    JSONObject itemTags = tags.getJSONObject(0);
                    String author = itemTags.getString("webTitle");
                    stories.add(new Story(
                            item.getString("sectionName"),
                            item.getString("webTitle"),
                            item.getString("webPublicationDate"),
                            author,
                            item.getString("webUrl")));
                } else {
                    stories.add(new Story(
                            item.getString("sectionName"),
                            item.getString("webTitle"),
                            item.getString("webPublicationDate"),
                            item.getString("webUrl")));
                }

            }
            // Return the list of earthquakes
            return stories;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return null;
    }
}
