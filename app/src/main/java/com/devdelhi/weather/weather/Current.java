package com.devdelhi.weather.weather;

import com.devdelhi.weather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Current {
    public String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipitation;
    private String mSummary;

    public String getmTimeZone() {
        return mTimeZone;
    }

    public void setmTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

    private String mTimeZone;

    public String getmIcon() {
        return mIcon;
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;

        if(mIcon.equals("clear-Day"))
            iconId = R.drawable.clear_day;
        else if (mIcon.equals("clear-night")){
            iconId = R.drawable.clear_night;
        }
        else if (mIcon.equals("rain")){
            iconId = R.drawable.rain;
        }
        else if (mIcon.equals("snow")){
            iconId = R.drawable.snow;
        }
        else if (mIcon.equals("sleet")){
            iconId = R.drawable.sleet;
        }
        else if (mIcon.equals("wind")){
            iconId = R.drawable.wind;
        }
        else if (mIcon.equals("fog")){
            iconId = R.drawable.fog;
        }
        else if (mIcon.equals("cloudy")){
            iconId = R.drawable.fog;
        }
        else if (mIcon.equals("partly-cloudy-day")){
            iconId = R.drawable.partly_cloudy;
        }
        else if (mIcon.equals("partly-cloudy-night")){
            iconId = R.drawable.cloudy_night;
        }

        return iconId;

    }


    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public long getmTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getmTimeZone()));
        Date dateTime = new Date(getmTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmTemperature() {
        mTemperature = ((mTemperature - 32)*5)/9;
        return (int)Math.round(mTemperature);
    }

    public void setmTemperature(double mTemperature) {
        this.mTemperature = mTemperature;
    }

    public double getmHumidity() {
        return mHumidity;
    }

    public void setmHumidity(double mHumidity) {
        this.mHumidity = mHumidity;
    }

    public double getmPrecipitation() {
        double precipPercentage = mPrecipitation * 100;
        return mPrecipitation;
    }

    public void setmPrecipitation(double mPrecipitation) {
        this.mPrecipitation = mPrecipitation;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
}
