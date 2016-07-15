package com.nguyen.andy.kisetsu;

/**
 * Created by Andy on 7/12/2016.
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
