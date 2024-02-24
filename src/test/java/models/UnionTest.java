package models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import rw.rutaks.deja_vu.models.UnionTE;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnionTest {

    RangeEveryYearTE juneToSepRange = RangeEveryYearTE.of(Month.JUNE, Month.SEPTEMBER);
    DayInMonthTE firstSundayOfMonth = DayInMonthTE.of(DayOfWeek.SUNDAY, 1);

    UnionTE union = UnionTE.of(juneToSepRange, firstSundayOfMonth);

    @Test
    void unionIncludesAnyDayInRange() {
        assertTrue(union.includes(LocalDate.of(2023, 6, 1)));
        assertTrue(union.includes(LocalDate.of(2023, 6, 30)));
        assertTrue(union.includes(LocalDate.of(2023, 9, 1)));
        assertTrue(union.includes(LocalDate.of(2023, 9, 30)));
    }

    @Test
    void unionIncludesAnyDayInMonthMatch() {
        assertTrue(union.includes(LocalDate.of(2024, 1, 7)));
        assertTrue(union.includes(LocalDate.of(2024, 2, 4)));
        assertTrue(union.includes(LocalDate.of(2024, 4, 7)));
        assertTrue(union.includes(LocalDate.of(2025, 1, 5)));
    }

    @Test
    void unionIncludesDayInMonthAndRangeMatch() {
        assertTrue(union.includes(LocalDate.of(2023, 6, 3)));
        assertTrue(union.includes(LocalDate.of(2023, 7, 1)));
        assertTrue(union.includes(LocalDate.of(2023, 8, 5)));
        assertTrue(union.includes(LocalDate.of(2023, 9, 2)));
    }

    @Test
    void unionExcludesOtherDates() {
        assertFalse(union.includes(LocalDate.of(2023, 4, 18)));
        assertFalse(union.includes(LocalDate.of(2024, 2, 6)));
        assertFalse(union.includes(LocalDate.of(2025, 4, 7)));
        assertFalse(union.includes(LocalDate.of(2021, 5, 11)));
    }

    @Test
    void ofThrowsOnNullList() {
        assertThrows(NullPointerException.class, () -> UnionTE.of((List<TemporalExpression>) null));
    }

    @Test
    void ofThrowsOnNullExpression() {
        assertThrows(NullPointerException.class, () -> UnionTE.of((TemporalExpression) null));
    }

    @Test
    void ofThrowsOnNullExpressionArray() {
        assertThrows(NullPointerException.class, () -> UnionTE.of((TemporalExpression[]) null));
    }

    @Test
    void ofThrowsOnEmptyList() {
        assertThrows(IllegalArgumentException.class, () -> UnionTE.of(new ArrayList<>()));
    }

    @Test
    void expressionsCannotBeModified() {
        List<TemporalExpression> expressions = new ArrayList<>();
        expressions.add(juneToSepRange);
        expressions.add(firstSundayOfMonth);
        UnionTE modifyMe = UnionTE.of(expressions);
        // Show that a date in the range matches
        assertTrue(modifyMe.includes(LocalDate.of(2023, 6, 1)));
        expressions.remove(juneToSepRange);
        // Show that a date in the range still matches
        assertTrue(modifyMe.includes(LocalDate.of(2023, 6, 1)));
    }
}
