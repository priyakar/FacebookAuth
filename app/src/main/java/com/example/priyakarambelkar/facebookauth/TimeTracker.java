package com.example.priyakarambelkar.facebookauth;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTracker {
    public Calendar currentDate = Calendar.getInstance();
    public long seconds = currentDate.getTimeInMillis();
    int dayOfMonth = Calendar.DAY_OF_MONTH;
    int month = Calendar.MONTH;
    int year = Calendar.YEAR;
    Date date = new Date();

    public Calendar getCurrentDate() {
        return currentDate;
    }

    public String getSeconds() {

        return String.valueOf(currentDate.getTimeInMillis());
    }

    public void setCurrentDate(int currentDate) {
        this.currentDate.set(Calendar.YEAR, currentDate);
    }

}
