package com.nguyen.andy.kisetsu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import com.nguyen.andy.kisetsu.adapters.SeasonsListAdapter;

// Kisetsu
// 季節

/**
 * This is the main activity that opens up on the start of the app. Displays a list of Seasons,
 * starting from two seasons in the future and going backwards until there are 15 seasons listed.
 */
public class SeasonsCatalogActivity extends AppCompatActivity {
    // constants
    private static final String TAG = "SeasonsCatalogActivity";
    private static final int NUM_SEASONS = 15;
    private static final int NUM_UPCOMING_SEASONS = 2;
    private static final int YEAR_LENGTH = 12;
    private static final int SEASON_LENGTH = 3;

    // fields
    private ArrayList<SeasonItem> seasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons_catalog);
        setTitle("Kisetsu");

        initSeasons();

        // Set the adapter to the season catalog layout and populate the UI wiht seasons
        final ListView seasonsCatalogView = (ListView) findViewById(R.id.seasons_catalog);
        seasonsCatalogView.setAdapter(new SeasonsListAdapter(this, seasons));
        seasonsCatalogView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeasonItem seasonData = (SeasonItem) seasonsCatalogView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), AnimeCatalogActivity.class);
                intent.putExtra("Season", seasonData.getSeason());
                intent.putExtra("Year", seasonData.getYear());

                // open the AnimeCatalogActivity for this season
                startActivity(intent);
            }
        });
    }

    /**
     * Initializes the list of SeasonItems given the current date.
     */
    private void initSeasons() {
        seasons = new ArrayList<SeasonItem>();

        // Retrieve current month and year
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        // Calculate what the month/year will be in months
        int nextSeasonMonth = month + NUM_UPCOMING_SEASONS * SEASON_LENGTH;
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
