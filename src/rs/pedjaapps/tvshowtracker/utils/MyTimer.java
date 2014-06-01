package rs.pedjaapps.tvshowtracker.utils;

import android.util.Log;

import java.text.DecimalFormat;

import rs.pedjaapps.tvshowtracker.BuildConfig;

public class MyTimer
{
    private long timerStart = 0;
    private long timerStartTotal = 0;
    private long iterations = 0;
    private double timeElapsed;

    public MyTimer()
    {
        timerStart = System.currentTimeMillis();
        timerStartTotal = System.currentTimeMillis();
    }

    private String getFormatted(double number)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
        return decimalFormat.format(number);
    }

    String get()
    {
        iterations++;
        timeElapsed = 1.0 * (System.currentTimeMillis() - timerStart) / 1000; // seconds
        String result = " [time:" + getFormatted(timeElapsed) + " seconds]";
        timerStart = System.currentTimeMillis();
        return result;
    }

    String avg()
    {
        timeElapsed = 1.0 * (System.currentTimeMillis() - timerStartTotal) / 1000 / iterations; // seconds
        return " [time:" + getFormatted(timeElapsed) + " seconds]";
    }

    String total()
    {
        timeElapsed = 1.0 * (System.currentTimeMillis() - timerStartTotal) / 1000; // seconds
        return " [time:" + getFormatted(timeElapsed) + " seconds]";
    }

    public void log(String message)
    {
        if(BuildConfig.DEBUG) Log.d(Constants.LOG_TAG, message + get());
    }

    public void logAvg(String message)
    {
        if(BuildConfig.DEBUG) Log.d(Constants.LOG_TAG, message + avg());
    }

    public void logTotal(String message)
    {
        if(BuildConfig.DEBUG) Log.d(Constants.LOG_TAG, message + total());
    }
}
