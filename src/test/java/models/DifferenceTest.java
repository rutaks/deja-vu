package models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.DifferenceTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class DifferenceTest {

    private final TemporalExpression months = RangeEveryYearTE.of(Month.JUNE, Month.SEPTEMBER);
    private final TemporalExpression days = DayInMonthTE.of(DayOfWeek.TUESDAY, -1);

    private final TemporalExpression difference = DifferenceTE.of(months, days);

    private final LocalDate invalidDate1 = LocalDate.of(2023, 6, 1);
    private final LocalDate invalidDate2 = LocalDate.of(2023, 7, 1);
    private final LocalDate invalidDate3 = LocalDate.of(2023, 8, 1);
    private final LocalDate invalidDate4 = LocalDate.of(2023, 9, 1);
    private final LocalDate invalidDate5 = LocalDate.of(2023, 6, 28);
    private final LocalDate invalidDate6 = LocalDate.of(2023, 6, 25);

    private final LocalDate validLastTuesdayOfMonth1 = LocalDate.of(2023, 6, 27);
    private final LocalDate validLastTuesdayOfMonth2 = LocalDate.of(2023, 7, 25);
    private final LocalDate validLastTuesdayOfMonth3 = LocalDate.of(2023, 8, 29);
    private final LocalDate validLastTuesdayOfMonth4 = LocalDate.of(2023, 9, 26);

    @Test
    void includesExpectedDays() {
        assertTrue(difference.includes(invalidDate1));
        assertTrue(difference.includes(invalidDate2));
        assertTrue(difference.includes(invalidDate3));
        assertTrue(difference.includes(invalidDate4));
        assertTrue(difference.includes(invalidDate5));
        assertTrue(difference.includes(invalidDate6));
    }

    @Test
    void excludesExpectedDays() {
        assertFalse(difference.includes(validLastTuesdayOfMonth1));
        assertFalse(difference.includes(validLastTuesdayOfMonth2));
        assertFalse(difference.includes(validLastTuesdayOfMonth3));
        assertFalse(difference.includes(validLastTuesdayOfMonth4));
    }
}
