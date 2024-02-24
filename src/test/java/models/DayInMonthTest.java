package models;

import rw.rutaks.deja_vu.models.DayInMonthTE;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DayInMonthTest {

    /**
     * Second Monday in month
     */
    private final DayInMonthTE secondMondayOfTheMonth = DayInMonthTE.of(DayOfWeek.MONDAY, 2);

    /**
     * Expression that should be equal to {@link #secondMondayOfTheMonth}
     */
    private final DayInMonthTE same1 = DayInMonthTE.of(DayOfWeek.MONDAY, 2);

    /**
     * Expression that should be unequal to {@link #secondMondayOfTheMonth}
     */
    private final DayInMonthTE thirdSundayOfTheMonth = DayInMonthTE.of(DayOfWeek.SUNDAY, 3);

    /**
     * Second Monday in June 2023
     */
    private final LocalDate match1 = LocalDate.of(2023, 6, 12);

    /**
     * Second Monday in August 2023
     */
    private final LocalDate match2 = LocalDate.of(2023, 8, 14);

    /**
     * Tests that expression matches known-good dates.
     */
    @Test
    void expressionMatchesSecondMondays() {
        assertTrue(secondMondayOfTheMonth.includes(match1));
        assertTrue(secondMondayOfTheMonth.includes(match2));
    }

    @Test
    void expressionDoesNotMatchOtherDates() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        while (date.isBefore(date.with(TemporalAdjusters.lastDayOfMonth()))) {
            if (!date.equals(match1)) {
                assertFalse(secondMondayOfTheMonth.includes(date));
            }
            date = date.plusDays(1);
        }
    }

    @Test
    void equalsIsReflexive() {
        assertTrue(secondMondayOfTheMonth.equals(secondMondayOfTheMonth));
    }

    @Test
    void equalsIsSymmetric() {
        assertEquals(secondMondayOfTheMonth, secondMondayOfTheMonth);
        assertEquals(same1, secondMondayOfTheMonth);
    }

    @Test
    void equalsHandlesNull() {
        assertNotEquals(null, secondMondayOfTheMonth);
    }

    @Test
    void hashCodeIsConsistent() {
        int hashCode = secondMondayOfTheMonth.hashCode();
        for (int i = 0; i < 1000; i++) {
            assertEquals(hashCode, secondMondayOfTheMonth.hashCode());
        }
    }

    @Test
    void equalObjectsHaveEqualHashCodes() {
        assertEquals(secondMondayOfTheMonth.hashCode(), same1.hashCode());
    }

    @Test
    void unequalObjectsHaveUnequalHashCodes() {
        assertNotEquals(secondMondayOfTheMonth.hashCode(), thirdSundayOfTheMonth.hashCode());
    }

    @Test
    public void ofThrowsOnNullDayOfWeek() {
        assertThrows(NullPointerException.class, () -> DayInMonthTE.of(null, 1));
    }

    @Test
    void ofThrowsOnZeroOrdinal() {
        assertThrows(IllegalArgumentException.class, () -> DayInMonthTE.of(DayOfWeek.MONDAY, 0));
    }

    @Test
    void ofThrowsOnOrdinalOver5() {
        assertThrows(IllegalArgumentException.class, () -> DayInMonthTE.of(DayOfWeek.MONDAY, 6));
    }

    @Test
    void ofThrowsOnOrdinalUnderMinus5() {
        assertThrows(IllegalArgumentException.class, () -> DayInMonthTE.of(DayOfWeek.MONDAY, -6));
    }
}
