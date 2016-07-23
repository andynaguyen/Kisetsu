package com.nguyen.andy.kisetsu;

/**
 * Represents an Anime series for the AnimeCatalogActivity and AnimeListParser
 */
public class AnimeItem {

    private String title;
    private String imageUrl;
    private String malURL;

    public AnimeItem(String title, String imageUrl, String malURL) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.malURL = malURL;
    }

    /***** PUBLIC ACCESSORS *****/
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMalURL() {
        return  malURL;
    }
    /***** END OF PUBLIC ACCESSORS *****/
}
