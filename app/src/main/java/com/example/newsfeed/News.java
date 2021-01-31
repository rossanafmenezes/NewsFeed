package com.example.newsfeed;

public class News {

    /** information for each article (title, author, date and section)*/
    private String mHeadline;
    private String mAuthor;
    private String mDate;
    private String mSection;
    private String mUrl;

    /** Constructs a new {@link News} object.
     *
     * @param headline
     * @param author
     * @param date
     * @param section
     */

    public News (String headline, String author, String date, String section, String url) {

        mHeadline = headline;
        mAuthor = author;
        mDate = date;
        mSection = section;
        mUrl = url;

    }

    /**
     * Returns the information for each article
     */

    public String getmHeadline() {

        return mHeadline;
    }

    public String getAuthor() {

        return mAuthor;
    }

    public String getDate(){

        return mDate;
    }

    public String getSection() {

        return mSection;
    }

    public String getUrl() {

        return mUrl;
    }
}

