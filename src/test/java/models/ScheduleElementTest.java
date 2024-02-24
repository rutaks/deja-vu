package models;

import rw.rutaks.deja_vu.interfaces.Schedule;
import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.IntersectionTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import rw.rutaks.deja_vu.models.ScheduleElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleElementTest {

    private final TemporalExpression secondMondayOfMonth = DayInMonthTE.of(DayOfWeek.MONDAY, 2);
    private final TemporalExpression janToJune = RangeEveryYearTE.of(Month.JANUARY, Month.JUNE);

    private final IntersectionTE intersection = IntersectionTE.of(secondMondayOfMonth, janToJune);

    private final ScheduleElement element = ScheduleElement.of(intersection, 100);

    private final LocalDate expectedSecondMondayOfMonth1 = LocalDate.of(2023, 1, 9);
    private final LocalDate expectedSecondMondayOfMonth2 = LocalDate.of(2023, 2, 13);
    private final LocalDate expectedSecondMondayOfMonth3 = LocalDate.of(2023, 3, 13);

    private final LocalDate invalidDate1 = LocalDate.of(2023, 7, 9);
    private final LocalDate invalidDate2 = LocalDate.of(2023, 8, 13);
    private final LocalDate invalidDate3 = LocalDate.of(2023, 9, 10);

    @Test
    void includesExpectedDates() {
        assertTrue(element.isOccurring(expectedSecondMondayOfMonth1));
        assertTrue(element.isOccurring(expectedSecondMondayOfMonth2));
        assertTrue(element.isOccurring(expectedSecondMondayOfMonth3));
    }

    @Test
    void hasSlotsForCorrectDates() {
        Schedule schedule = Schedule.of(element);
        Assertions.assertEquals(100, schedule.slots(expectedSecondMondayOfMonth3));
        Assertions.assertEquals(0, schedule.slots(invalidDate1));
    }

    @Test
    void excludesExpectedDates() {
        assertFalse(element.isOccurring(invalidDate1));
        assertFalse(element.isOccurring(invalidDate2));
        assertFalse(element.isOccurring(invalidDate3));
    }
}
