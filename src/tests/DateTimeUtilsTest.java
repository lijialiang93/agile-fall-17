package tests;

import utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilsTest {

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    @org.junit.jupiter.api.Test
    void calculateAge() {
        try {
            Date birth = sdf.parse("01-01-1900");
            Date death = sdf.parse("01-01-2000");
            assertEquals(DateTimeUtils.calculateAge(birth, death), 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void calculateYears() {
        try {
            Date start = sdf.parse("01-01-1920");
            Date end = sdf.parse("01-01-1967");
            assertEquals(DateTimeUtils.calculateYears(start, end), 47);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void calculateTimeSpan() {
        try {
            Date start = sdf.parse("01-01-1993");
            Date end = sdf.parse("03-01-1996");

            Period period = DateTimeUtils.calculateTimeSpan(start, end);
            assertEquals(period.getMonths(), 2);
            assertEquals(period.getDays(), 0);
            assertEquals(period.getYears(), 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}