package com.nguyen.andy.kisetsu;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Andy on 7/12/2016.
 */
public class AnimeItem {
    enum MediaType {
        TV,
        ONA,
        OVA,
        MOVIE,
        SPECIAL
    }

    private String name;
    private String imageUrl;
    private String snyopsis;
    private ArrayList<String> genres;
    private MediaType mediaType;
    private int numEps;

    /***** PUBLIC ACCESSORS *****/
    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSnyopsis() {
        return snyopsis;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public int getNumEps() {
        return numEps;
    }
    /***** END OF PUBLIC ACCESSORS *****/

    public AnimeItem() {

    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static AnimeItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, AnimeItem.class);
    }
}
