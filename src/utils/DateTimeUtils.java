package utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    public static int calculateAge(Date birthday, Date death) {
        if (death == null) {
            return calculateYears(birthday, new Date());
        } else {
            return calculateYears(birthday, death);
        }
    }

    public static int calculateYears(Date start, Date end) {
        return calculateTimeSpan(start, end).getYears();
    }
    
    // Abdul start
    public static long calculateAgeInDays(Date birthday) {
                	
            long diff = new Date().getTime() - birthday.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            return diff;

    }
    // Abdul end
    
    public static Period calculateTimeSpan(Date start, Date end) {
        LocalDate lo = new java.sql.Date(start.getTime()).toLocalDate();
        LocalDate hi = new java.sql.Date(end.getTime()).toLocalDate();
        return Period.between(lo, hi);
    }

}
