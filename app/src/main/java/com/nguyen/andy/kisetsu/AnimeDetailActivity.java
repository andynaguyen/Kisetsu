package com.nguyen.andy.kisetsu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class AnimeDetailActivity extends AppCompatActivity {
    private String malUrl;
    private String imgUrl;
    private String title;
    private String season;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        Bundle bundle = getIntent().getExtras();
        malUrl = bundle.getString("MalUrl");
        imgUrl = bundle.getString("ImgUrl");
        title  = bundle.getString("Title");
        season = bundle.getString("SeasonFrom");
        year = bundle.getInt("YearFrom");

        TextView titleView = (TextView) findViewById(R.id.detail_title);
        titleView.setText(malUrl);
        Log.d("url", "mal " + malUrl);
        titleView.setTextColor(Color.BLACK);


        //TODO: Parse malurl for info
        // synopsis, rating, mediatype, studio, genre(s), epcount, air dates
        //TODO: Open in browser button on main bar
        //TODO: handle intent on back button pressed
    }
}
