package models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.IntersectionTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    private final TemporalExpression secondMondayOfMonth = DayInMonthTE.of(DayOfWeek.MONDAY, 2);
    private final TemporalExpression janToJuneRange = RangeEveryYearTE.of(Month.JANUARY, Month.JUNE);

    private final IntersectionTE secondMondaysOfTheMonthFromJanToJune_Intersection = IntersectionTE.of(secondMondayOfMonth, janToJuneRange);

    private final LocalDate expectedSecondMondayOfMonth1 = LocalDate.of(2023, 1, 9);
    private final LocalDate expectedSecondMondayOfMonth2 = LocalDate.of(2023, 2, 13);
    private final LocalDate expectedSecondMondayOfMonth3 = LocalDate.of(2023, 3, 13);
    private final LocalDate expectedSecondMondayOfMonth4 = LocalDate.of(2023, 4, 10);
    private final LocalDate expectedSecondMondayOfMonth5 = LocalDate.of(2023, 5, 8);
    private final LocalDate expectedSecondMondayOfMonth6 = LocalDate.of(2023, 6, 12);

    private final LocalDate invalidDate1 = LocalDate.of(2023, 7, 9);
    private final LocalDate invalidDate2 = LocalDate.of(2023, 8, 13);
    private final LocalDate invalidDate3 = LocalDate.of(2023, 9, 10);
    private final LocalDate invalidDate4 = LocalDate.of(2023, 10, 9);
    private final LocalDate invalidDate5 = LocalDate.of(2023, 11, 12);
    private final LocalDate invalidDate6 = LocalDate.of(2023, 12, 10);

    private final LocalDate invalidDate7 = LocalDate.of(2023, 1, 1);
    private final LocalDate invalidDate8 = LocalDate.of(2023, 2, 1);
    private final LocalDate invalidDate9 = LocalDate.of(2023, 3, 1);
    private final LocalDate invalidDate10 = LocalDate.of(2023, 4, 1);
    private final LocalDate invalidDate11 = LocalDate.of(2023, 5, 1);
    private final LocalDate invalidDate12 = LocalDate.of(2023, 6, 1);

    @Test
    void includesExpectedDates() {
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth1));
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth2));
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth3));
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth4));
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth5));
        assertTrue(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(expectedSecondMondayOfMonth6));
    }

    @Test
    void excludesExpectedDates() {
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate1));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate2));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate3));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate4));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate5));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate6));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate7));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate8));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate9));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate10));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate11));
        assertFalse(secondMondaysOfTheMonthFromJanToJune_Intersection.includes(invalidDate12));
    }

    @Test
    void ofThrowsOnNullList() {
        assertThrows(NullPointerException.class, () -> IntersectionTE.of((List<TemporalExpression>) null));
    }

    @Test
    void ofThrowsOnNullExpression() {
        assertThrows(NullPointerException.class, () -> IntersectionTE.of((TemporalExpression) null));
    }

    @Test
    void ofThrowsOnNullExpressionArray() {
        assertThrows(NullPointerException.class, () -> IntersectionTE.of((TemporalExpression[]) null));
    }

    @Test
    void ofThrowsOnEmptyExpressions() {
        assertThrows(IllegalArgumentException.class, () -> IntersectionTE.of(new ArrayList<>()));
    }

    @Test
    void expressionsCannotBeModified() {
        List<TemporalExpression> expressions = new ArrayList<>();
        expressions.add(secondMondayOfMonth);
        expressions.add(janToJuneRange);
        IntersectionTE modifyMe = IntersectionTE.of(expressions);
        assertTrue(modifyMe.includes(expectedSecondMondayOfMonth1));

        // Test if intersection is idle after modification of assigned expression
        expressions.remove(secondMondayOfMonth);
        expressions.remove(janToJuneRange);
        assertTrue(modifyMe.includes(expectedSecondMondayOfMonth1));
    }
}
