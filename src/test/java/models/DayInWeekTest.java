package models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInWeekTE;
import rw.rutaks.deja_vu.models.UnionTE;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DayInWeekTest {

    private static final LocalDate SAT = LocalDate.of(2023, 8, 12);

    private static final LocalDate SUN = LocalDate.of(2023, 8, 13);

    private static final LocalDate MON = LocalDate.of(2023, 8, 14);

    private static final LocalDate THU = LocalDate.of(2023, 8, 17);

    @Test
    void ofThrowsOnNullDayOfWeek() {
        assertThrows(NullPointerException.class, () -> DayInWeekTE.of(null));
    }

    @Test
    void includesReturnsTrueOnMatch() {
        DayInWeekTE sat = DayInWeekTE.of(DayOfWeek.SATURDAY);
        assertTrue(sat.includes(SAT));
        DayInWeekTE sun = DayInWeekTE.of(DayOfWeek.SUNDAY);
        assertTrue(sun.includes(SUN));
    }

    @Test
    void includesReturnsFalseOnNonMatch() {
        DayInWeekTE sat = DayInWeekTE.of(DayOfWeek.SATURDAY);
        assertFalse(sat.includes(SUN));
        DayInWeekTE sun = DayInWeekTE.of(DayOfWeek.SUNDAY);
        assertFalse(sun.includes(SAT));
    }

    @Test
    void canCombineDaysInWeek() {
        TemporalExpression everyMonAndThu = UnionTE.of(DayInWeekTE.of(DayOfWeek.MONDAY), DayInWeekTE.of(DayOfWeek.THURSDAY));
        assertTrue(everyMonAndThu.includes(MON));
        assertTrue(everyMonAndThu.includes(THU));
        assertTrue(everyMonAndThu.includes(MON.minusWeeks(1)));
        assertTrue(everyMonAndThu.includes(THU.minusWeeks(1)));
        assertTrue(everyMonAndThu.includes(MON.plusWeeks(1)));
        assertTrue(everyMonAndThu.includes(THU.plusWeeks(0)));
        assertFalse(everyMonAndThu.includes(SAT));
        assertFalse(everyMonAndThu.includes(SUN));
    }

    @Test
    void ofThrowsOnNonPositiveOrdinal() {
        assertThrows(IllegalArgumentException.class, () -> DayInWeekTE.of(0, SAT));
        assertThrows(IllegalArgumentException.class, () -> DayInWeekTE.of(-1, SAT));
        assertThrows(IllegalArgumentException.class, () -> DayInWeekTE.of(-10, SAT));
    }

    @Test
    void ofThrowsOnNullReferenceDate() {
        assertThrows(NullPointerException.class, () -> DayInWeekTE.of(1, null));
    }

    @Test
    void dayInWeekWithOrdinalIncludesReturnsTrueOnMatch() {
        // Every second Monday from MON
        DayInWeekTE everySecondMonday = DayInWeekTE.of(2, MON);
        assertTrue(everySecondMonday.includes(MON));
        assertTrue(everySecondMonday.includes(MON.plusWeeks(2)));
        assertTrue(everySecondMonday.includes(MON.plusWeeks(4)));
        assertTrue(everySecondMonday.includes(MON.plusWeeks(12)));
        assertTrue(everySecondMonday.includes(MON.minusWeeks(2)));
        assertTrue(everySecondMonday.includes(MON.minusWeeks(4)));
        assertTrue(everySecondMonday.includes(MON.minusWeeks(12)));
    }

    @Test
    void dayInWeekWithOrdinalIncludesReturnsFalseOnNonMatch() {
        // Every second Monday from MON
        DayInWeekTE everySecondMonday = DayInWeekTE.of(2, MON);
        assertFalse(everySecondMonday.includes(SAT));
        assertFalse(everySecondMonday.includes(SUN));
        assertFalse(everySecondMonday.includes(THU));
        assertFalse(everySecondMonday.includes(MON.plusWeeks(1)));
        assertFalse(everySecondMonday.includes(MON.plusWeeks(3)));
        assertFalse(everySecondMonday.includes(MON.plusWeeks(11)));
        assertFalse(everySecondMonday.includes(MON.minusWeeks(1)));
        assertFalse(everySecondMonday.includes(MON.minusWeeks(3)));
        assertFalse(everySecondMonday.includes(MON.minusWeeks(11)));
    }
}
