package com.example.android.newsapp;

public class Story {
    private String sectionName;
    private String title;
    private String timeDate;
    private String author;

    private String url;

    /**
     * Constructs a new {@link Story} object.
     *
     * @param vsectionName  is the section name
     * @param vtitle   is the title of particular story
     * @param vtimeDate is the String that contains time and date when
     * story was published  and must be separate in adapter
     * @param vauthor is the author of the story
     * @param vurl  is the link to the story on the Guardian website
     */

    Story (String vsectionName, String vtitle, String vtimeDate, String vauthor,String vurl) {
        sectionName = vsectionName;
        title = vtitle;
        timeDate = vtimeDate;
        author = vauthor;
        url = vurl;
    }

    Story (String vsectionName, String vtitle, String vtimeDate,String vurl) {
        sectionName = vsectionName;
        title = vtitle;
        timeDate = vtimeDate;
        url = vurl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public String getDate() {
        return timeDate;
    }

    public String getAuthor() {
        return author;
    }

    public boolean hasAuthor() {
        return author != null;
    }


    public String getUrl() {
        return url;
    }

}
