package com.nguyen.andy.kisetsu;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import com.bluejamesbond.text.DocumentView;
import java.util.HashMap;

import com.nguyen.andy.kisetsu.parsers.DetailParser;

public class AnimeDetailActivity extends AppCompatActivity {
    // constants
    private static final int SYNOPSIS_PADDING_LEFT = 54;
    private static final int SYNOPSIS_PADDING_TOP = 30;
    private static final int SYNOPSIS_PADDING_RIGHT = 42;
    private static final int SYNOPSIS_PADDING_BOTTOM = 42;
    private static final HashMap<String, String> RATING_MAP; // simplifies the parsed Ratings

    static {
        RATING_MAP = new HashMap<String, String>();
        RATING_MAP.put("R - 17+ (violence & profanity)", "R-17+");
        RATING_MAP.put("R+ - Mild Nudity", "R-17+");
        RATING_MAP.put("PG-13 - Teens 13 or older", "PG-13");
        RATING_MAP.put("G - All Ages", "G");
        RATING_MAP.put("None", "None");
    }

    // fields
    private String malUrl;
    private String imgUrl;
    private String title;
    private String summary;
    private String seasonFrom;
    private int yearFrom;

    ProgressDialog progessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_detail);

        // unpack variables from intent
        Bundle bundle = getIntent().getExtras();
        malUrl = bundle.getString("MalUrl");
        imgUrl = bundle.getString("ImgUrl");
        title  = bundle.getString("Title");
        seasonFrom = bundle.getString("SeasonFrom");
        yearFrom = bundle.getInt("YearFrom");

        setTitle("[  ] " + title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // make FAB
        FloatingActionButton detailFab = (FloatingActionButton) findViewById(R.id.detail_fab);
        detailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSummary();
            }
        });

        // load image for thumbnail and set onclicklistener
        ImageView thumbnailView= (ImageView) findViewById(R.id.thumbnail);
        Picasso.with(getApplicationContext())
                .load(imgUrl)
                .fit()
                .into(thumbnailView);
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupZoom();
            }
        });

        // set onclicklistener for TAP FOR ZOOM
        TextView tapForZoomView = (TextView) findViewById(R.id.detail_tap_to_zoom);
        tapForZoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupZoom();
            }
        });

        // ViewFlipper
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
        RelativeLayout scoreLayout = (RelativeLayout) findViewById(R.id.score_stats_layout);
        RelativeLayout otherLayout = (RelativeLayout) findViewById(R.id.other_stats_layout);

        scoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });

        otherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });

        // if connected to internet, start web scraping.
        // else, make toast
        if (ConnectivityCheck.isConnectedToInternet(getApplicationContext())) {
            new ParseURLTask().execute(malUrl);
        } else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mal_link:
                Uri uri = Uri.parse(malUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates a new popup dialog that shows a zoomed in picture of the thumbnail.
     */
    private void popupZoom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimeDetailActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        LayoutInflater factory = LayoutInflater.from(AnimeDetailActivity.this);

        final View lview = factory.inflate(R.layout.image_dialog, null);
        ImageView dialogImg = (ImageView) lview.findViewById(R.id.dialog_image);
        Picasso.with(getApplicationContext())
                .load(imgUrl)
                .fit()
                .centerInside()
                .into(dialogImg);
        builder.setView(lview);
        builder.show();
    }

    /**
     * Creates a new popup dialog that shows the summary.
     */
    private void popupSummary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimeDetailActivity.this);
        LayoutInflater factory = LayoutInflater.from(AnimeDetailActivity.this);

        final View lview = factory.inflate(R.layout.synopsis_dialog, null);
        DocumentView synopsis = (DocumentView) lview.findViewById(R.id.dialog_synopsis);
        synopsis.setText(summary);

        builder.setView(lview);
        builder.show();
    }

    /**
     * Shows the next stats table (slides left to right)
     */
    private void showNext() {
        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right));
        flipper.showNext();
    }

    // asyncTask dedicated to parsing the HTML
    private class ParseURLTask extends AsyncTask<String, Void, DetailParser> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // start progressDialog
            progessDialog = new ProgressDialog(AnimeDetailActivity.this);
            progessDialog.setTitle("Fetching data");
            progessDialog.setMessage("Loading...");
            progessDialog.setIndeterminate(false);
            progessDialog.show();
        }

        @Override
        protected DetailParser doInBackground(String... params) {
            return new DetailParser(params[0]);
        }

        @Override
        protected void onPostExecute(DetailParser parser) {
            // get all fields needed to display
            String synopsis = parser.parseSynopsis();

            HashMap<String, String> info = parser.parseGeneralInfo();
            String score = info.get("Score");
            String ranked = info.get("Ranked");
            String popularity = info.get("Popularity");
            String type = info.get("Type");
            String status = info.get("Status");
            String aired = info.get("Aired");
            String rating = info.get("Rating");
            String episodes = info.get("Episodes");
            String duration = info.get("Duration");
            String source = info.get("Source");
            String studios = info.get("Studios");
            String genres = info.get("Genres");

            // set title to be "[__TYPE__] TITLE"
            setTitle("[" + type + "] " + title);
            AnimeDetailActivity.this.summary = synopsis;

            // modify the strings for score and ranked
            score = score.split("\\*")[0];
            ranked = ranked.split("\\*")[0];
            rating = RATING_MAP.get(rating);

            // set text for all the TextViews
            TextView titleTV = (TextView) findViewById(R.id.detail_title);
            titleTV.setText(title);
            TextView scoreTV = (TextView) findViewById(R.id.detail_score);
            scoreTV.setText(score);
            TextView rankedTV = (TextView) findViewById(R.id.detail_ranked);
            rankedTV.setText(ranked);
            TextView popularityTV = (TextView) findViewById(R.id.detail_popularity);
            popularityTV.setText(popularity);
            TextView typeTV = (TextView) findViewById(R.id.detail_media_type);
            typeTV.setText(type);
            TextView statusTV = (TextView) findViewById(R.id.detail_status);
            statusTV.setText(status);
            TextView airedTV = (TextView) findViewById(R.id.detail_aired_on);
            airedTV.setText(aired);
            TextView ratingTV = (TextView) findViewById(R.id.detail_rating);
            ratingTV.setText(rating);
            TextView episodesTV = (TextView) findViewById(R.id.detail_episodse);
            episodesTV.setText(episodes);
            TextView durationTV = (TextView) findViewById(R.id.detail_duration);
            durationTV.setText(duration);
            TextView sourceTV = (TextView) findViewById(R.id.detail_source);
            sourceTV.setText(source);
            TextView studiosTV = (TextView) findViewById(R.id.detail_studios);
            studiosTV.setText(studios);
            TextView genresTV = (TextView) findViewById(R.id.detail_genres);
            genresTV.setText(genres);

            progessDialog.dismiss();
        }
    }
}