package com.nguyen.andy.kisetsu.parsers;

import android.widget.ArrayAdapter;

import com.nguyen.andy.kisetsu.AnimeItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Andy on 7/20/2016.
 */
public class AnimeCatalogParser {
    private Document doc;

    public AnimeCatalogParser(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FOR TESTING PURPOSES
    public AnimeCatalogParser(File file, String url) {
        try {
            doc = Jsoup.parse(file, "UTF-8", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FOR TESTING PURPOSES
    public AnimeCatalogParser(Document doc) {
        this.doc = doc;
    }

    public ArrayList<AnimeItem> parseAnimeList() {
        ArrayList<AnimeItem> returnList = new ArrayList<AnimeItem>();
        Elements animeDivs = doc.select("div[style=\"\"][data-bg]");

        for (Element div : animeDivs) {
            returnList.add(new AnimeItem(parseTitle(div), parseImgUrl(div), parseMalUrl(div)));
        }

        return returnList;
    }

    // parses the div representing an anime series, and returns the title for that anime
    public static String parseTitle(Element animeDiv) {
        Elements titles = animeDiv.select("div[class=title]");

        if (titles.first() != null) {
            return titles.first().text();
        } else {
            return "ERROR FETCHING TITLE";
        }
    }

    // parses the div representing an anime series, and return the url for that anime's cover image
    public static String parseImgUrl(Element animeDiv) {
        if (animeDiv.attr("data-bg") != null) {
            return animeDiv.attr("data-bg");
        } else {
            return "ERROR FETCHING IMAGE";
        }
    }

    // parses the div representing an anime series, and returns the url for the MAL detail page
    public static String parseMalUrl(Element animeDiv) {
        Elements thumbLinks = animeDiv.select("a[href][class=thumb]");

        if (thumbLinks.first() != null) {
            return thumbLinks.first().attr("href");
        } else {
            return "ERROR FETCHING MAL URL";
        }
    }
}
