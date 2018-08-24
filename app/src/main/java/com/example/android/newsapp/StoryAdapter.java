package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class StoryAdapter extends ArrayAdapter<Story> {


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context          The current context. Used to inflate the layout file.
     * @param stories           A List of Story objects to display in a list
     */

    public StoryAdapter(@NonNull Activity context, List<Story> stories) {
        super(context, R.layout.list_item, stories);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the {@link Earthquake} object located at this position in the list
        final Story story = getItem(position);

        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            assert story != null;
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        if (story != null) {
            TextView sectionNameView = convertView.findViewById(R.id.section_name);
            sectionNameView.setText(story.getSectionName());
        }

        if (story != null) {
            TextView titleView = convertView.findViewById(R.id.title);
            titleView.setText(story.getTitle());
        }

        if (story != null) {
            // Find the TextView with view ID time
            TextView timeView = convertView.findViewById(R.id.time);

            // Find the TextView with view ID date
            TextView dateView = convertView.findViewById(R.id.date);

            String[] parts = story.getTimeDate().split("T");
            // Format the date string (i.e. "Mar 3, 1984")
            String date = parts[0].replaceAll("-",".");
            // Display the date of the current story in that TextView
            dateView.setText(date);
            //  Format the time string (i.e. "16:30")
            StringBuilder stringBuilder = new StringBuilder(parts[1]);
            stringBuilder.delete(5,9);


            timeView.setText(stringBuilder.toString());

        }

        if (story != null) {
            TextView authorView = convertView.findViewById(R.id.author);
            if (story.hasAuthor()) {
                authorView.setText(story.getAuthor());
            } else {
                authorView.setText("");
            }
        }

        // Return the whole list item layout (containing TextViews)
        // so that it can be shown in the ListView
        return convertView;
    }

}
