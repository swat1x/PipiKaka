package ru.swat1x.pipikaka.util;

import lombok.experimental.UtilityClass;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@UtilityClass
public class TextUtil {

    public String getDateFormat(long time) {
        Calendar date = GregorianCalendar.getInstance();
        date.setTime(new Date(System.currentTimeMillis() + time));
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);

        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minutes = date.get(Calendar.MINUTE);

        return String.format("%s.%s.%s в %s:%s", getFormatTimePart(day),
                getFormatTimePart(month), year,
                getFormatTimePart(hour), getFormatTimePart(minutes));
    }

    private String getFormatTimePart(int i) {
        return (i < 10 ? "0" : "") + i;
    }

    public String getTimeLabel(final long time) {
        return getTimeLabel(0, time);
    }

    public String getTimeLabel(final long start, final long end) {
        StringBuilder text = new StringBuilder();
        long value = end - start;
        long var;
        long dur = 86400000L;

        var = value / dur;
        boolean moreThatDay = var >= 1;
        if (moreThatDay) {
            if (!text.toString().equalsIgnoreCase("")) {
                text.append(" ");
            }
            text.append(var).append("д");
        }

        value = value - (dur * var);
        dur = 3600000L;
        var = value / dur;
        if (var >= 1) {
            if (!text.toString().equalsIgnoreCase("")) {
                text.append(" ");
            }
            text.append(var).append("ч");
        }

        value = value - (dur * var);
        dur = 60000L;
        var = value / dur;
        if (var >= 1 && !moreThatDay) {
            if (!text.toString().equalsIgnoreCase("")) {
                text.append(" ");
            }
            text.append(var).append("м");
        }

        value = value - (dur * var);
        dur = 1000L;
        var = value / dur;
        if (var >= 1 && !moreThatDay) {
            if (!text.toString().equalsIgnoreCase("")) {
                text.append(" ");
            }
            text.append(var).append("с");
        }

        long mls = end - start;
        if (mls < 1000L) {
            return mls + "мс";
        }

        return text.toString();
    }


}
