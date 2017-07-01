package ru.tinted_knight.sberbanksms.Tools;

import java.util.Calendar;

public class DebugTimer {

    private long startTime;

    private long stopTime = 0;

    public String inSeconds(){
        return String.valueOf(calculateLong() / 1_000);
    }

    public String inMillis(){
        return String.valueOf(calculateLong());
    }

    public void start(){
        startTime = getTime();
//        Slog.log("timer.start = " + startTime);
    }

    public void stop(){
        stopTime = getTime();
    }

    private long getTime(){
        return Calendar.getInstance().getTimeInMillis();
    }

    private long calculateLong(){
        if (stopTime == 0){
            long duration = getTime() - startTime;
            startTime = 0;
//            Slog.log("timer.duration = " + duration);
            return duration;
        }
        long duration = stopTime - startTime;
        stopTime = 0;
        startTime = 0;
        return duration;
    }
}
