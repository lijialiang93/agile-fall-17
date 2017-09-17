package utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

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

    public static Period calculateTimeSpan(Date start, Date end) {
        LocalDate lo = new java.sql.Date(start.getTime()).toLocalDate();
        LocalDate hi = new java.sql.Date(end.getTime()).toLocalDate();
        return Period.between(lo, hi);
    }

}
