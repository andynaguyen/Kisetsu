package com.nguyen.andy.kisetsu;

import java.util.HashMap;

/**
 * Created by Andy on 7/7/2016.
 */

public class SeasonItem {
    enum Season {
        SUMMER,
        FALL,
        WINTER,
        SPRING
    }

    public static final HashMap<Integer, Season> MONTH_TO_SEASON;
    private static final int YEAR_LENGTH = 12;
    private static final int SEASON_LENGTH = 3;

    static {
        MONTH_TO_SEASON = new HashMap<Integer, Season>();
        MONTH_TO_SEASON.put(1, Season.WINTER);
        MONTH_TO_SEASON.put(2, Season.WINTER);
        MONTH_TO_SEASON.put(3, Season.SPRING);
        MONTH_TO_SEASON.put(4, Season.SPRING);
        MONTH_TO_SEASON.put(5, Season.SPRING);
        MONTH_TO_SEASON.put(6, Season.SUMMER);
        MONTH_TO_SEASON.put(7, Season.SUMMER);
        MONTH_TO_SEASON.put(8, Season.SUMMER);
        MONTH_TO_SEASON.put(9, Season.FALL);
        MONTH_TO_SEASON.put(10, Season.FALL);
        MONTH_TO_SEASON.put(11, Season.FALL);
        MONTH_TO_SEASON.put(12, Season.WINTER);
    }

    private String name;
    private Season season;
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
                this.setTimeframe("June " + year + " to Aug " + year);
                break;
            case FALL:
                this.name = "Fall " + year;
                this.setTimeframe("Sept " + year + " to Nov " + year);
                break;
            case WINTER:
                this.name = "Winter " + year;
                this.setTimeframe("Dec " + (year - 1) + " to Feb " + year);
                break;
            case SPRING:
                this.name = "Spring " + year;
                this.setTimeframe("Mar " + year + " to May " + year);
                break;
            default:
                this.name = "WHY";
                this.setTimeframe("IS THIS HAPPENING");
                break;
        }
    }

    /***** PUBLIC ACCESSORS *****/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
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