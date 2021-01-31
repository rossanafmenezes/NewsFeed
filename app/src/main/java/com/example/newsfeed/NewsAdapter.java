package com.example.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter (Context context, ArrayList<News> newsArticles) {
        super(context, 0, newsArticles);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        //Get the News object located at this position in the list
        News currentNews = getItem(position);

        //Find the TextView in the list_item layout with the ID name headline
        TextView headlineTextView =  (TextView) listItemView.findViewById(R.id.headline);
        //Get the headline (title) from the currentNews object and set this text on the Headline Text view
        headlineTextView.setText(currentNews.getmHeadline());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(currentNews.getAuthor());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentNews.getDate());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        sectionTextView.setText(currentNews.getSection());

        return listItemView;

    }

    // convert milliseconds into regular date format
    private String formatDate(String date) {
        Date dateObject = new Date ();
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.US );
            dateObject = simpleDateFormat.parse ( date );
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat ( "MMM dd, yyyy ", Locale.US );
        String dateFormatted = newDateFormat.format ( dateObject );
        return dateFormatted;
    }

}
