package com.nguyen.andy.kisetsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

// Kisetsu
// 季節
public class SeasonsActivity extends AppCompatActivity {
    // constants
    private static final String TAG = "SeasonsActivity";
    private static final int NUM_SEASONS = 7;
    private static final int YEAR_LENGTH = 12;
    private static final int SEASON_LENGTH = 3;

    // fields
    private ArrayList<SeasonItem> seasons;

    // TODO: Make method to get season from data
    // TODO: Open a new activity on item click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);
        setTitle("Kisetsu");

        initSeasons();

        final ListView seasonsListView = (ListView) findViewById(R.id.seasons_list);
        seasonsListView.setAdapter(new CustomListAdapter(this, seasons));
        seasonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeasonItem seasonData = (SeasonItem) seasonsListView.getItemAtPosition(position);
                //String seasonJson = seasonData.toJson();

                //Toast.makeText(getApplicationContext(), seasonJson, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), AnimeListActivity.class);
                intent.putExtra("Season", seasonData.getSeason().toString());
                intent.putExtra("Year", seasonData.getYear());

                startActivity(intent);
            }
        });
    }

    private void initSeasons() {
        seasons = new ArrayList<SeasonItem>();

        // Retrieve current month and year
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        // Calculate what the month/year will be in 3 months
        int nextSeasonMonth = month + SEASON_LENGTH;
        int nextSeasonYear = year;

        if (nextSeasonMonth > YEAR_LENGTH) {
            nextSeasonMonth = nextSeasonMonth % YEAR_LENGTH;
            nextSeasonYear++;
        }

        // Create SeasonItems starting from next season and going backwards,
        // until NUM_SEASONS objects have been created
        SeasonItem currSeason = new SeasonItem(nextSeasonMonth, nextSeasonYear);
        for (int i = 0; i < NUM_SEASONS; currSeason = currSeason.getPrevSeason(), i++) {
            seasons.add(currSeason);
        }
    }



}
