package mobidev.parkingfinder;

import android.content.Context;

public class TimeUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    
    private TimeUtils() {
    }

    /**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration, Context c) {
      StringBuffer res = new StringBuffer();
      String days = c.getString(R.string.days);
      String hours = c.getString(R.string.hours);
      String minutes = c.getString(R.string.minutes);
      String seconds = c.getString(R.string.seconds);
      String and = c.getString(R.string.and);
      
      long temp = 0;
      if (duration >= ONE_SECOND) {
        temp = duration / ONE_DAY;
        if (temp > 0) {
          duration -= temp * ONE_DAY;
          res.append(temp).append(days).append(duration >= ONE_MINUTE ? ", " : "");
        }

        temp = duration / ONE_HOUR;
        if (temp > 0) {
          duration -= temp * ONE_HOUR;
          res.append(temp).append(hours).append(duration >= ONE_MINUTE ? ", " : "");
        }

        temp = duration / ONE_MINUTE;
        if (temp > 0) {
          duration -= temp * ONE_MINUTE;
          res.append(temp).append(minutes);
        }

        if (!res.toString().equals("") && duration >= ONE_SECOND) {
          res.append(and);
        }

        temp = duration / ONE_SECOND;
        if (temp > 0) {
          res.append(temp).append(seconds);
        }
        return res.toString();
      } else {
        return "0" + seconds;
      }
    }
}