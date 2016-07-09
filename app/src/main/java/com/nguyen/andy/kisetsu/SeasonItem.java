package com.nguyen.andy.kisetsu;

/**
 * Created by Andy on 7/7/2016.
 */

public class SeasonItem {
    private String name;
    private Season season;
    private String timeframe;

    public SeasonItem() {
        // empty
    }

    public SeasonItem(Season season, int year){
        this.season = season;
        switch (season) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }
}