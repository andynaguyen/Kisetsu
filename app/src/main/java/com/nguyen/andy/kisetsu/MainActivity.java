package com.nguyen.andy.kisetsu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
// Kisetsu
// 季節
public class MainActivity extends AppCompatActivity {
    // constants
    private static final String TAG = "MainActivity";
    private static final int MAX_SEASONS = 4;

    // fields
    private ArrayList<SeasonItem> seasons;

    // TODO: Make method to get season from data
    // TODO: Open a new activity on item click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Kisetsu");

        initSeasons();

        final ListView seasonsView = (ListView) findViewById(R.id.seasons_list);
        seasonsView.setAdapter(new CustomListAdapter(this, seasons));
        seasonsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SeasonItem seasonData = (SeasonItem) seasonsView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, "Selected : " + seasonData, Toast.LENGTH_LONG);
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
