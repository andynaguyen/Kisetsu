package com.nguyen.andy.kisetsu.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Responsible for connecting to the URL and scraping the page's contents for information needed
 * for the AnimeDetailActivity, using JSoup.
 */
public class DetailParser {
    private Document doc;

    public DetailParser(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FOR TESTING PURPOSES
    public DetailParser(File file, String url) {
        try {
            doc = Jsoup.parse(file, "UTF-8", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FOR TESTING PURPOSES
    public DetailParser(Document doc) {
        this.doc = doc;
    }

    // return the synopsis for the detail page
    public String parseSynopsis() {
        Element synopsis = doc.select("meta[property=og:description]").first();

        if (synopsis != null && !synopsis.equals("")) {
            return synopsis.attr("content");
        } else {
            return "ERROR FETCHING SYNOPSIS";
        }
    }

    // return the general info found in the detail page
    public HashMap<String, String> parseGeneralInfo() {
        HashMap<String, String> info = new HashMap<String, String>();
        Element detailsTable = doc.select("div[class=Detail]").first();
        Elements detailRows = detailsTable.select("tr");

        for (Element tr : detailRows) {
            Elements td = tr.select("td");
            String key, val;
            if (td.get(0) == null) continue;

            key = td.get(0).text();

            if (td.get(1) != null) {
                val = td.get(1).text();
            } else {
                val = "ERROR FETCHING " + td.get(0);
            }

            //Log.d("HashMap", "k: " + key + ", val: " + val);
            info.put(key, val);
        }

        return info;
    }
}