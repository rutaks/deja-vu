package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.Schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BasicSchedule implements Schedule {

    private final List<ScheduleElement> elements;

    public BasicSchedule(List<ScheduleElement> elements) {
        Objects.requireNonNull(elements);
        this.elements = Collections.unmodifiableList(new ArrayList<>(elements));
    }

    @Override
    public boolean isOccurring(LocalDate date) {
        for (ScheduleElement e : elements) {
            if (e.isOccurring(date)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int slots(LocalDate date) {
        for (ScheduleElement e : elements) {
            if (e.isOccurring(date)) {
                return e.slots();
            }
        }
        return 0;
    }

    @Override
    public List<LocalDate> datesInRange(LocalDate start, LocalDate end) {
        List<LocalDate> result = new ArrayList<>();
        LocalDate cursor = start;
        while (cursor.equals(end) || cursor.isBefore(end)) {
            if (isOccurring(cursor)) {
                result.add(cursor);
            }
            cursor = cursor.plusDays(1);
        }
        return result;
    }

    @Override
    public LocalDate nextOccurrence(LocalDate date) {
        LocalDate cursor = date;
        while (!isOccurring(cursor)) {
            cursor = cursor.plusDays(1);
        }
        return cursor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
            .append('[')
            .append(this.getClass().getSimpleName())
            .append(": ")
            .append(elements.stream().map(e -> e.toString()).collect(Collectors.joining(", ")))
            .append(']');
        return sb.toString();
    }

    @Override
    public Stream<LocalDate> futureDates(LocalDate start) {
        return Stream.iterate(nextOccurrence(start), seed -> nextOccurrence(seed.plusDays(1)));
    }

    @Override
    public Stream<LocalDate> pastDates(LocalDate start) {
        return Stream.iterate(previousOccurrence(start), seed -> previousOccurrence(seed.minusDays(1)));
    }

    @Override
    public LocalDate previousOccurrence(LocalDate date) {
        LocalDate cursor = date;
        while (!isOccurring(cursor)) {
            cursor = cursor.minusDays(1);
        }
        return cursor;
    }
}
