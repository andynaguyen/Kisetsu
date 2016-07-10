package com.nguyen.andy.kisetsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AnimeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_list);

        Bundle bundle = getIntent().getExtras();
        String seasonJson = bundle.getString("SeasonItem");
        SeasonItem seasonData = SeasonItem.fromJson(seasonJson);

        TextView jsonTV = (TextView) findViewById(R.id.json_text);
        jsonTV.setText(seasonJson);

        TextView seasonItemTV = (TextView) findViewById(R.id.season_item_text);
        String tmp = seasonData.getName() + seasonData.getSeason() + seasonData.getTimeframe();
        seasonItemTV.setText(tmp);

        TextView seasonToStringTV = (TextView) findViewById(R.id.season_toString);
        seasonToStringTV.setText(seasonData.toString());
    }
}
