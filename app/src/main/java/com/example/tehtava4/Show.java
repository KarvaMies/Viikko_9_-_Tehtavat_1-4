package com.example.tehtava4;

import java.util.Date;

public class Show {

    private Date date;
    private String title;
    private String headline;
    private boolean inTimeFrame;

    public Show(String name, Date showStart, Date showEnd, String headline) {
        title = name;
        this.date = date;
        this.headline = headline;
    }

    public String getHeadline() {
        return headline;
    }

    public void setInTimeFrameTrue() {
        inTimeFrame = true;
    }

    public void setInTimeFrameFalse() {
        inTimeFrame = false;
    }

    public boolean isInTimeFrame() {
        return inTimeFrame;
    }
}