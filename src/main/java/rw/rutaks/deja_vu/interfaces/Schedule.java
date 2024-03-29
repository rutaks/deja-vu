package rw.rutaks.deja_vu.interfaces;

import rw.rutaks.deja_vu.models.BasicSchedule;
import rw.rutaks.deja_vu.models.ScheduleElement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Encapsulates a "schedule" which can answer various questions about events it
 * knows about.
 */
public interface Schedule {
    /**
     * Is {@code event} occurring on {@code date}?
     *
     * @param date a {@link LocalDate}
     * @return {@code true} if {@code event} is occurring on {@code date}, otherwise
     *         {@code false}
     */
    boolean isOccurring(LocalDate date);

    /**
     * Get {@code slots} available on specific {@code date}
     * Provides 0 if date not found in schedule
     *
     * @param date a {@link LocalDate}
     * @return no. Slots generated for that date
     */
    int slots(LocalDate date);

    /**
     * Returns a list of {@link LocalDate}s on which {@code event} is occurring
     * between {@code start} and {@code end} dates (inclusive).
     *
     * @param start start date
     * @param end end date
     * @return list of dates on which {@code event} is occurring
     */
    List<LocalDate> datesInRange(LocalDate start, LocalDate end);

    /**
     * Returns a stream of future {@link LocalDate}s on which {@code event} is
     * occurring beginning at {@code start} (inclusive) and proceeding forward in
     * time.
     *
     * @param start start date
     * @return stream of dates on which {@code event} is occurring
     */
    Stream<LocalDate> futureDates(LocalDate start);

    /**
     * Returns a stream of past {@link LocalDate}s on which {@code event} is
     * occurring beginning at {@code start} (inclusive) and proceeding backwards in
     * time.
     *
     * @param start start date
     * @return stream of dates on which {@code event} is occurring
     */
    Stream<LocalDate> pastDates(LocalDate start);

    /**
     * Returns the next date on which {@code event} is occurring, on or after
     * {@code date}.
     *
     * @param date an arbitrary date
     * @return date of next occurrence of {@code event}
     */
    LocalDate nextOccurrence(LocalDate date);

    /**
     * Returns the previous date on which {@code event} is occurring, on or before
     * {@code date}.
     *
     * @param date an arbitrary date
     * @return date of previous occurrence of {@code event}
     */
    LocalDate previousOccurrence(LocalDate date);

    /**
     * Returns a {@code Schedule} composed of the supplied {@code elements}.
     *
     * @param elements {@link ScheduleElement}s comprising the {@link Schedule}
     * @return a {@code Schedule}
     * @throws NullPointerException if any {@link ScheduleElement} in {@code elements} is {@code null}
     */
    static Schedule of(ScheduleElement... elements) {
        for (ScheduleElement e : elements) {
            Objects.requireNonNull(e);
        }
        return new BasicSchedule(Arrays.asList(elements));
    }

    /**
     * Returns a {@code Schedule} composed of the {@link ScheduleElement}s in {@code elements}.
     *
     * @param elements a {@link List} of {@link ScheduleElement}s
     * @return a {@code Schedule}
     * @throws NullPointerException if {@code elements} is {@code null}
     */
    static Schedule of(List<ScheduleElement> elements) {
        Objects.requireNonNull(elements);
        return new BasicSchedule(elements);
    }
}
