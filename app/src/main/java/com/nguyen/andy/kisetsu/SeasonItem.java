package com.nguyen.andy.kisetsu;

import java.util.HashMap;

/**
 * Created by Andy on 7/7/2016.
 */

public class SeasonItem {
    public static final String SUMMER = "Summer";
    public static final String FALL   = "Fall";
    public static final String WINTER = "Winter";
    public static final String SPRING = "Spring";

    public static final HashMap<Integer, String> MONTH_TO_SEASON;
    private static final int YEAR_LENGTH = 12;
    private static final int SEASON_LENGTH = 3;

    static {
        MONTH_TO_SEASON = new HashMap<Integer, String>();
        MONTH_TO_SEASON.put(1, WINTER);
        MONTH_TO_SEASON.put(2, WINTER);
        MONTH_TO_SEASON.put(3, SPRING);
        MONTH_TO_SEASON.put(4, SPRING);
        MONTH_TO_SEASON.put(5, SPRING);
        MONTH_TO_SEASON.put(6, SUMMER);
        MONTH_TO_SEASON.put(7, SUMMER);
        MONTH_TO_SEASON.put(8, SUMMER);
        MONTH_TO_SEASON.put(9, FALL);
        MONTH_TO_SEASON.put(10, FALL);
        MONTH_TO_SEASON.put(11, FALL);
        MONTH_TO_SEASON.put(12, WINTER);
    }

    private String name;
    private String season;
    private String timeframe;
    private int month;
    private int year;

    public SeasonItem(int month, int year){
        this.month = month;
        this.year = year;
        this.season = MONTH_TO_SEASON.get(month);

        switch (this.season) {
            case SUMMER:
                this.name = "Summer " + year;
                this.timeframe = "June " + year + " to Aug " + year;
                break;
            case FALL:
                this.name = "Fall " + year;
                this.timeframe = "Sept " + year + " to Nov " + year;
                break;
            case WINTER:
                this.name = "Winter " + year;
                this.timeframe = "Dec " + (year - 1) + " to Feb " + year;
                break;
            case SPRING:
                this.name = "Spring " + year;
                this.timeframe = "Mar " + year + " to May " + year;
                break;
            default:
                this.name = "WHY";
                this.timeframe = "IS THIS HAPPENING";
                break;
        }
    }

    /***** PUBLIC ACCESSORS *****/
    public String getName() {
        return name;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public String getSeason() {
        return season;
    }

    public int getYear() {
        return this.year;
    }
    /***** END OF PUBLIC ACCESSORS *****/

    /*
     * Creates a new SeasonItem object representing the previous season of this season.
     */
    public SeasonItem getPrevSeason() {
        SeasonItem prevSeason = null;

        int prevSeasonMonth = this.month - SEASON_LENGTH;
        int prevSeasonYear = this.year;

        if (prevSeasonMonth < 1) {
            prevSeasonMonth += YEAR_LENGTH;
            prevSeasonYear--;
        }

        return new SeasonItem(prevSeasonMonth, prevSeasonYear);
    }

    @Override
    public String toString() {
        return this.name + " from " + this.timeframe;
    }
}