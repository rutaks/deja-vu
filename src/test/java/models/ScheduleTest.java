package models;

import rw.rutaks.deja_vu.interfaces.Schedule;
import rw.rutaks.deja_vu.interfaces.TemporalExpression;
import rw.rutaks.deja_vu.models.DayInMonthTE;
import rw.rutaks.deja_vu.models.IntersectionTE;
import rw.rutaks.deja_vu.models.RangeEveryYearTE;
import rw.rutaks.deja_vu.models.ScheduleElement;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private final TemporalExpression secondMondayOfMonth = DayInMonthTE.of(DayOfWeek.MONDAY, 2);
    private final TemporalExpression janToJune = RangeEveryYearTE.of(Month.JANUARY, Month.JUNE);

    private final IntersectionTE intersection = IntersectionTE.of(secondMondayOfMonth, janToJune);

    private final ScheduleElement element = ScheduleElement.of(intersection, 0);

    private final LocalDate expectedSecondMondayOfMonth1 = LocalDate.of(2023, 1, 9);
    private final LocalDate expectedSecondMondayOfMonth2 = LocalDate.of(2023, 2, 13);
    private final LocalDate expectedSecondMondayOfMonth3 = LocalDate.of(2023, 3, 13);

    private final LocalDate invalidDate1 = LocalDate.of(2023, 7, 9);
    private final LocalDate invalidDate2 = LocalDate.of(2023, 8, 13);
    private final LocalDate invalidDate3 = LocalDate.of(2023, 9, 10);

    private final Schedule schedule = Schedule.of(element);

    private final List<LocalDate> expectedFutureDates = new ArrayList<>(
        Arrays.asList(
            LocalDate.of(2024, 1, 8),
            LocalDate.of(2024, 2, 12),
            LocalDate.of(2024, 3, 11),
            LocalDate.of(2024, 4, 8),
            LocalDate.of(2024, 5, 13),
            LocalDate.of(2024, 6, 10),
            LocalDate.of(2025, 1, 13),
            LocalDate.of(2025, 2, 10),
            LocalDate.of(2025, 3, 10),
            LocalDate.of(2025, 4, 14)
        )
    );

    @Test
    void includesExpectedDatesForKnownEvent() {
        assertTrue(schedule.isOccurring(expectedSecondMondayOfMonth1));
        assertTrue(schedule.isOccurring(expectedSecondMondayOfMonth2));
        assertTrue(schedule.isOccurring(expectedSecondMondayOfMonth3));
    }

    @Test
    void excludesExpectedDatesForUnknownEvent() {
        assertFalse(schedule.isOccurring(invalidDate1));
        assertFalse(schedule.isOccurring(invalidDate2));
        assertFalse(schedule.isOccurring(invalidDate3));
    }

    @Test
    void excludesAllDatesForUnknownEvent() {
        assertFalse(schedule.isOccurring(invalidDate1));
        assertFalse(schedule.isOccurring(invalidDate2));
        assertFalse(schedule.isOccurring(invalidDate3));
    }

    @Test
    void datesInRangeReturnsExpectedDatesForYear() {
        List<LocalDate> result = schedule.datesInRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        assertEquals(6, result.size());
        assertTrue(result.contains(LocalDate.of(2024, 1, 8)));
        assertTrue(result.contains(LocalDate.of(2024, 2, 12)));
        assertTrue(result.contains(LocalDate.of(2024, 3, 11)));
        assertTrue(result.contains(LocalDate.of(2024, 4, 8)));
        assertTrue(result.contains(LocalDate.of(2024, 5, 13)));
        assertTrue(result.contains(LocalDate.of(2024, 6, 10)));
    }

    @Test
    void nextOccurrenceReturnsExpectedResults() {
        assertEquals(LocalDate.of(2024, 1, 8), schedule.nextOccurrence(LocalDate.of(2023, 6, 30)));
        assertEquals(LocalDate.of(2024, 1, 8), schedule.nextOccurrence(LocalDate.of(2023, 9, 1)));
        assertEquals(LocalDate.of(2023, 1, 9), schedule.nextOccurrence(LocalDate.of(2023, 1, 8)));
        assertEquals(LocalDate.of(2023, 1, 9), schedule.nextOccurrence(LocalDate.of(2023, 1, 1)));
        assertEquals(LocalDate.of(2023, 1, 9), schedule.nextOccurrence(LocalDate.of(2023, 1, 9)));
        assertEquals(LocalDate.of(2023, 2, 13), schedule.nextOccurrence(LocalDate.of(2023, 2, 13)));
        assertEquals(LocalDate.of(2024, 1, 8), schedule.nextOccurrence(LocalDate.of(2023, 12, 20)));
    }

    @Test
    void futureDatesProducesExpectedResult() {
        List<LocalDate> futureDates = schedule.futureDates(LocalDate.of(2023, 9, 1)).limit(10).collect(Collectors.toList());
        assertEquals(expectedFutureDates, futureDates);
    }

    @Test
    void previousOccurrenceReturnsExpectedResults() {
        assertEquals(LocalDate.of(2024, 6, 10), schedule.previousOccurrence(LocalDate.of(2024, 6, 30)));
        assertEquals(LocalDate.of(2024, 6, 10), schedule.previousOccurrence(LocalDate.of(2024, 9, 1)));
        assertEquals(LocalDate.of(2024, 6, 10), schedule.previousOccurrence(LocalDate.of(2024, 12, 20)));
        assertEquals(LocalDate.of(2022, 6, 13), schedule.previousOccurrence(LocalDate.of(2023, 1, 1)));
        assertEquals(LocalDate.of(2022, 6, 13), schedule.previousOccurrence(LocalDate.of(2023, 1, 8)));
        assertEquals(LocalDate.of(2023, 1, 9), schedule.previousOccurrence(LocalDate.of(2023, 1, 9)));
        assertEquals(LocalDate.of(2023, 2, 13), schedule.previousOccurrence(LocalDate.of(2023, 2, 13)));
    }

    @Test
    void pastDatesProducesExpectedResult() {
        List<LocalDate> expectedPastDates = new ArrayList<>(expectedFutureDates);
        Collections.reverse(expectedPastDates);
        List<LocalDate> pastDates = schedule.pastDates(LocalDate.of(2025, 5, 1)).limit(10).collect(Collectors.toList());
        assertEquals(expectedPastDates, pastDates);
    }

    @Test
    void elementsCannotBeModified() {
        List<ScheduleElement> elements = new ArrayList<>();
        elements.add(element);
        Schedule modifyMe = Schedule.of(elements);
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth1));
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth2));
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth3));
        // Remove element--should have no effect
        elements.remove(element);
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth1));
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth2));
        assertTrue(modifyMe.isOccurring(expectedSecondMondayOfMonth3));
    }

    @Test
    void ofThrowsOnNullList() {
        assertThrows(NullPointerException.class, () -> Schedule.of((List<ScheduleElement>) null));
    }

    @Test
    void ofAllowsEmptyList() {
        Schedule schedule = Schedule.of(new ArrayList<>());
        // Just test some operations
        assertFalse(schedule.isOccurring(expectedSecondMondayOfMonth1));
        assertFalse(schedule.isOccurring(expectedSecondMondayOfMonth2));
        assertFalse(schedule.isOccurring(expectedSecondMondayOfMonth3));
        assertTrue(schedule.datesInRange(LocalDate.of(2024, 1, 1), LocalDate.of(2019, 12, 31)).isEmpty());
    }

    @Test
    void ofThrowsOnNullElement() {
        assertThrows(NullPointerException.class, () -> Schedule.of(element, null));
    }
}
