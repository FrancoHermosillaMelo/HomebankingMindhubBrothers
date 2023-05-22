package com.mindhub.homebanking.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class UtilsTransaction {
    public static LocalDateTime dateToLocalDateTime (Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public static String getStringDateFromLocalDateTime(LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return year + " / " + month + " / " + day;
    }
    public static String getStringHourFromLocalDateTime(LocalDateTime date){
        int hour = date.getHour();
        int min = date.getMinute();
        int sec = date.getSecond();

        return String.valueOf(hour<10?'0':"") + hour + ":" + (min<10?'0':"") + min + ":" + (sec<10?'0':"") + sec;
    }
}
