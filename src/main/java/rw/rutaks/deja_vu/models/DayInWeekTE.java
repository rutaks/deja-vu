package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class DayInWeekTE implements TemporalExpression {

    /**
     * Day of week
     */
    private final DayOfWeek dayOfWeek;

    /**
     * Ordinal describing every "nth" week
     */
    private final int ordinal;

    /**
     * Reference date if we're not using <em>every</em> {@code dayOfWeek}
     */
    private final LocalDate referenceDate;

    private DayInWeekTE(DayOfWeek dayOfWeek, int ordinal, LocalDate referenceDate) {
        this.dayOfWeek = dayOfWeek;
        this.ordinal = ordinal;
        this.referenceDate = referenceDate;
        // There are only two object states that make sense:
        // 1. dayOfWeek is set, ordinal == 0, and referenceDate is null; or
        // 2. referenceDate is set, ordinal > 0 and dayOfWeek is null.
        if (
            !((dayOfWeek != null && ordinal == 0 && referenceDate == null) || (referenceDate != null && ordinal > 0 && dayOfWeek == null))
        ) {
            throw new IllegalStateException(
                "Must have either dayOfWeek is set (and ordinal == 0, referenceDate is null), " +
                "or referenceDate is set, with ordinal > 0 (and dayOfWeek is null)."
            );
        }
    }

    public static DayInWeekTE of(DayOfWeek dayOfWeek) {
        Objects.requireNonNull(dayOfWeek);
        return new DayInWeekTE(dayOfWeek, 0, null);
    }

    public static DayInWeekTE of(int ordinal, LocalDate referenceDate) {
        Objects.requireNonNull(referenceDate);
        if (ordinal < 1) {
            throw new IllegalArgumentException("'ordinal' must be >= 1.");
        }
        return new DayInWeekTE(null, ordinal, referenceDate);
    }

    @Override
    public boolean includes(LocalDate date) {
        if (dayOfWeek == null) {
            return (
                Objects.equals(referenceDate.getDayOfWeek(), date.getDayOfWeek()) &&
                (ChronoUnit.WEEKS.between(referenceDate, date) % ordinal == 0)
            );
        } else {
            return Objects.equals(dayOfWeek, date.getDayOfWeek());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, ordinal, referenceDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DayInWeekTE)) {
            return false;
        }
        DayInWeekTE other = (DayInWeekTE) obj;
        return dayOfWeek == other.dayOfWeek && ordinal == other.ordinal && Objects.equals(referenceDate, other.referenceDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
            .append('[')
            .append(this.getClass().getSimpleName())
            .append(": dayOfWeek=")
            .append(dayOfWeek)
            .append(" ordinal=")
            .append(ordinal)
            .append(" referenceDate=")
            .append(referenceDate)
            .append(']');
        return sb.toString();
    }
}
