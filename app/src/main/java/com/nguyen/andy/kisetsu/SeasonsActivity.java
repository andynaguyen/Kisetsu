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

// Kisetsu
// 季節
public class SeasonsActivity extends AppCompatActivity {
    // constants
    private static final String TAG = "SeasonsActivity";
    private static final int MAX_SEASONS = 4;

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
                String seasonJson = seasonData.toJson();

                Toast.makeText(getApplicationContext(), seasonJson, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), AnimeListActivity.class);
                intent.putExtra("SeasonItem",seasonJson);

                startActivity(intent);
            }
        });
    }

    private void initSeasons() {
        seasons = new ArrayList<SeasonItem>();

        // TODO: Make it determine which seasons to list using the date
        Season[] catalog = { Season.SUMMER, Season.FALL, Season.WINTER, Season.SPRING};
        int[] years = { 2016, 2016, 2017, 2017 };

        for (int i = 0; i < MAX_SEASONS; i++) {
            SeasonItem seasonData = new SeasonItem(catalog[i], years[i]);
            seasons.add(seasonData);
        }
    }
}
