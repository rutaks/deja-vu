package models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;

import static org.junit.jupiter.api.Assertions.*;

class RangeEveryYearTest {

    private final RangeEveryYearTE dayMonthRange = RangeEveryYearTE.of(MonthDay.of(Month.MARCH, 12), MonthDay.of(Month.JULY, 20));
    private final RangeEveryYearTE monthsRange = RangeEveryYearTE.of(Month.APRIL, Month.AUGUST);
    private final RangeEveryYearTE monthRange = RangeEveryYearTE.of(Month.JUNE);

    private final RangeEveryYearTE fullRangeSame1 = RangeEveryYearTE.of(MonthDay.of(Month.MARCH, 12), MonthDay.of(Month.JULY, 20));
    private final RangeEveryYearTE monthsRangeSame1 = RangeEveryYearTE.of(Month.APRIL, Month.AUGUST);
    private final RangeEveryYearTE monthRangeSame1 = RangeEveryYearTE.of(Month.JUNE);

    private final RangeEveryYearTE fullRangeSame2 = RangeEveryYearTE.of(MonthDay.of(Month.MARCH, 12), MonthDay.of(Month.JULY, 20));
    private final RangeEveryYearTE monthsRangeSame2 = RangeEveryYearTE.of(Month.APRIL, Month.AUGUST);
    private final RangeEveryYearTE monthRangeSame2 = RangeEveryYearTE.of(Month.JUNE);

    private final RangeEveryYearTE fullRangeDiff1 = RangeEveryYearTE.of(MonthDay.of(Month.MARCH, 2), MonthDay.of(Month.DECEMBER, 25));
    private final RangeEveryYearTE monthsRangeDiff1 = RangeEveryYearTE.of(Month.JANUARY, Month.MAY);
    private final RangeEveryYearTE monthRangeDiff1 = RangeEveryYearTE.of(Month.SEPTEMBER);

    private final LocalDate fullRangeIn1 = LocalDate.of(2023, 3, 12);
    private final LocalDate fullRangeIn2 = LocalDate.of(2023, 7, 20);
    private final LocalDate fullRangeIn3 = LocalDate.of(2023, 6, 30);

    private final LocalDate fullRangeOut1 = LocalDate.of(2019, 8, 1);
    private final LocalDate fullRangeOut2 = LocalDate.of(2020, 1, 31);
    private final LocalDate fullRangeOut3 = LocalDate.of(2020, 3, 11);

    private final LocalDate monthsRangeIn1 = LocalDate.of(2023, 4, 1);
    private final LocalDate monthsRangeIn2 = LocalDate.of(2023, 8, 1);
    private final LocalDate monthsRangeIn3 = LocalDate.of(2023, 5, 11);
    
    
    private final LocalDate monthsRangeOut1 = LocalDate.of(2019, 3, 3);
    private final LocalDate monthsRangeOut2 = LocalDate.of(2020, 9, 9);

    private final LocalDate monthRangeIn1 = LocalDate.of(2023, 6, 1);
    private final LocalDate monthRangeIn2 = LocalDate.of(2023, 6, 30);
    private final LocalDate monthRangeIn3 = LocalDate.of(2020, 6, 12);
    
    private final LocalDate monthRangeOut1 = LocalDate.of(2019, 12, 31);
    private final LocalDate monthRangeOut2 = LocalDate.of(2020, 1, 1);

    @Test
    void includesReturnsTrueForDatesInFullRange() {
        assertEquals(true, dayMonthRange.includes(fullRangeIn1));
        assertEquals(true, dayMonthRange.includes(fullRangeIn2));
        assertEquals(true, dayMonthRange.includes(fullRangeIn3));
    }

    @Test
    void includesReturnsFalseForDatesNotInFullRange() {
        assertFalse(dayMonthRange.includes(fullRangeOut1));
        assertFalse(dayMonthRange.includes(fullRangeOut2));
        assertFalse(dayMonthRange.includes(fullRangeOut3));
    }

    @Test
    void includesReturnsTrueForDatesInMonthsRange() {
        assertEquals(true, monthsRange.includes(monthsRangeIn1));
        assertEquals(true, monthsRange.includes(monthsRangeIn2));
        assertEquals(true, monthsRange.includes(monthsRangeIn3));
    }

    @Test
    void includesReturnsFalseForDatesNotInMonthsRange() {
        assertFalse(monthsRange.includes(monthsRangeOut1));
        assertFalse(monthsRange.includes(monthsRangeOut2));
    }

    @Test
    void includesReturnsTrueForDatesInMonthRange() {
        assertEquals(true, monthRange.includes(monthRangeIn1));
        assertEquals(true, monthRange.includes(monthRangeIn2));
        assertEquals(true, monthRange.includes(monthRangeIn3));
    }

    @Test
    void includesReturnsFalseForDatesNotInMonthRange() {
        assertFalse(monthRange.includes(monthRangeOut1));
        assertFalse(monthRange.includes(monthRangeOut2));
    }

    @Test
    void equalsIsReflexive() {
        assertEquals(dayMonthRange, dayMonthRange);
        assertEquals(monthsRange, monthsRange);
        assertEquals(true, monthRange.equals(monthRange));
    }

    @Test
    void equalsIsSymmetric() {
        assertEquals(true, dayMonthRange.equals(fullRangeSame1));
        assertEquals(true, fullRangeSame1.equals(dayMonthRange));
        assertEquals(true, monthsRange.equals(monthsRangeSame1));
        assertEquals(true, monthsRangeSame1.equals(monthsRange));
        assertEquals(true, monthRange.equals(monthRangeSame1));
        assertEquals(true, monthRangeSame1.equals(monthRange));
    }

    @Test
    void equalsIsTransitive() {
        assertEquals(dayMonthRange, fullRangeSame1);
        assertEquals(fullRangeSame1, fullRangeSame2);
        assertEquals(dayMonthRange, fullRangeSame2);
        assertEquals(monthsRange, monthsRangeSame1);
        assertEquals(monthsRangeSame1, monthsRangeSame2);
        assertEquals(monthsRange, monthsRangeSame2);
        assertEquals(monthRange, monthRangeSame1);
        assertEquals(monthRangeSame1, monthRangeSame2);
        assertEquals(monthRange, monthRangeSame2);
    }

    @Test
    void equalsIsConsistent() {
        for (int i = 0; i < 1000; i++) {
            assertEquals(dayMonthRange, fullRangeSame1);
            assertEquals(monthsRange, monthsRangeSame1);
            assertEquals(monthRange, monthRangeSame1);
        }
    }

    @Test
    void equalsHandlesNull() {
        assertNotNull(dayMonthRange);
        assertNotNull(monthsRange);
        assertNotNull(monthRange);
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        assertEquals(dayMonthRange.hashCode(), fullRangeSame1.hashCode());
        assertEquals(dayMonthRange.hashCode(), fullRangeSame2.hashCode());
        assertEquals(monthsRange.hashCode(), monthsRangeSame1.hashCode());
        assertEquals(monthsRange.hashCode(), monthsRangeSame2.hashCode());
        assertEquals(monthRange.hashCode(), monthRangeSame1.hashCode());
        assertEquals(monthRange.hashCode(), monthRangeSame2.hashCode());
    }

    // This is not required by the contract, but is desirable
    @Test
    void unequalObjectsHaveUnequalHashCodes() {
        assertNotEquals(dayMonthRange.hashCode(), fullRangeDiff1.hashCode());
        assertNotEquals(monthsRange.hashCode(), monthsRangeDiff1.hashCode());
        assertNotEquals(monthRange.hashCode(), monthRangeDiff1.hashCode());
    }

    @Test
    void ofThrowsOnNullMonth() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of(null));
    }

    @Test
    void ofThrowsOnNullFirstMonth() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of(null, Month.APRIL));
    }

    @Test
    void ofThrowsOnNullSecondMonth() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of(Month.DECEMBER, null));
    }

    @Test
    void ofThrowsOnNullBothMonths() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of((Month) null, (Month) null));
    }

    @Test
    void ofThrowsOnNullFirstMonthDay() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of(null, MonthDay.of(Month.JULY, 1)));
    }

    @Test
    void ofThrowsOnNullSecondMonthDay() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of(MonthDay.of(Month.JUNE, 12), null));
    }

    @Test
    void ofThrowsOnNullBothMonthDays() {
        assertThrows(NullPointerException.class, () -> RangeEveryYearTE.of((MonthDay) null, (MonthDay) null));
    }

    @Test
    void canCreateRangeSpanningSingleDay() {
        TemporalExpression exp = RangeEveryYearTE.of(MonthDay.of(Month.SEPTEMBER, 1), MonthDay.of(Month.SEPTEMBER, 1));
        assertTrue(exp.includes(LocalDate.of(2023, 9, 1)));
        assertFalse(exp.includes(LocalDate.of(2023, 9, 2)));
    }

    @Test
    void canCreateRangeWithinSingleMonth() {
        TemporalExpression exp = RangeEveryYearTE.of(MonthDay.of(Month.SEPTEMBER, 1), MonthDay.of(Month.SEPTEMBER, 11));
        assertTrue(exp.includes(LocalDate.of(2023, 9, 1)));
        assertTrue(exp.includes(LocalDate.of(2023, 9, 5)));
        assertTrue(exp.includes(LocalDate.of(2023, 9, 6)));
        assertTrue(exp.includes(LocalDate.of(2023, 9, 11)));
        assertFalse(exp.includes(LocalDate.of(2023, 9, 12)));
        assertFalse(exp.includes(LocalDate.of(2023, 9, 13)));
        assertFalse(exp.includes(LocalDate.of(2023, 9, 21)));
        assertFalse(exp.includes(LocalDate.of(2023, 9, 30)));
        assertFalse(exp.includes(LocalDate.of(2023, 10, 12)));
    }
}
