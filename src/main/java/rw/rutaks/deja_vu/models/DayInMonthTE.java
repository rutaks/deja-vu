package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public final class DayInMonthTE implements TemporalExpression {

    /**
     * Maximum number of weeks in any month
     */
    private static final int MAX_WEEKS_IN_MONTH = 5;

    /**
     * Number of days in a week
     */
    private static final int DAYS_IN_WEEK = 7;

    /**
     * Ordinal position within month
     */
    private final int ordinal;

    /**
     * Day of week
     */
    private final DayOfWeek day;

    private DayInMonthTE(DayOfWeek day, int ordinal) {
        Objects.requireNonNull(day);
        if (ordinal == 0 || ordinal < -MAX_WEEKS_IN_MONTH || ordinal > MAX_WEEKS_IN_MONTH) {
            throw new IllegalArgumentException("ordinal=" + ordinal + " is not in [-5, 5] excluding 0.");
        }
        this.day = day;
        this.ordinal = ordinal;
    }

    public static DayInMonthTE of(DayOfWeek day, int ordinal) {
        return new DayInMonthTE(day, ordinal);
    }

    @Override
    public boolean includes(LocalDate date) {
        return dayMatches(date) && weekMatches(date);
    }

    private boolean dayMatches(LocalDate date) {
        return date.getDayOfWeek() == day;
    }

    private boolean weekMatches(LocalDate date) {
        if (ordinal > 0) {
            return weekFromStartMatches(date);
        } else {
            return weekFromEndMatches(date);
        }
    }

    private boolean weekFromStartMatches(LocalDate date) {
        return weekInMonth(date.getDayOfMonth()) == ordinal;
    }

    private boolean weekFromEndMatches(LocalDate date) {
        return weekInMonth(daysLeftInMonth(date) + 1) == Math.abs(ordinal);
    }

    private int weekInMonth(int dayOfMonth) {
        return ((dayOfMonth - 1) / DAYS_IN_WEEK) + 1;
    }

    private int daysLeftInMonth(LocalDate date) {
        return (int) date.until(date.with(TemporalAdjusters.lastDayOfMonth()), ChronoUnit.DAYS);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DayInMonthTE)) {
            return false;
        }
        DayInMonthTE other = (DayInMonthTE) o;
        return other.ordinal == ordinal && other.day == day;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        result = prime * result + ordinal;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.getClass().getSimpleName()).append(": day=").append(day).append(" ordinal=").append(ordinal).append(']');
        return sb.toString();
    }
}
