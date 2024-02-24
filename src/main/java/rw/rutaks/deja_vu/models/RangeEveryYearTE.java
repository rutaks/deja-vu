package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Objects;

public final class RangeEveryYearTE implements TemporalExpression {

    /**
     * First month of range
     */
    private final Month startMonth;

    /**
     * Last month of range
     */
    private final Month endMonth;

    /**
     * First day of range (in {@link #startMonth})
     */
    private final int startDay;

    /**
     * Last day of range (in {@link #endMonth})
     */
    private final int endDay;

    private RangeEveryYearTE(Month startMonth, Month endMonth, int startDay, int endDay) {
        Objects.requireNonNull(startMonth);
        Objects.requireNonNull(endMonth);
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        this.startDay = startDay;
        this.endDay = endDay;
        return;
    }

    public static RangeEveryYearTE of(MonthDay start, MonthDay end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        return new RangeEveryYearTE(start.getMonth(), end.getMonth(), start.getDayOfMonth(), end.getDayOfMonth());
    }

    public static RangeEveryYearTE of(Month startMonth, Month endMonth) {
        Objects.requireNonNull(startMonth);
        Objects.requireNonNull(endMonth);
        return new RangeEveryYearTE(startMonth, endMonth, 0, 0);
    }

    public static RangeEveryYearTE of(Month month) {
        Objects.requireNonNull(month);
        return new RangeEveryYearTE(month, month, 0, 0);
    }

    @Override
    public boolean includes(LocalDate date) {
        if (Objects.equals(startMonth, endMonth)) {
            return startMonthIncludes(date) && endMonthIncludes(date);
        } else {
            return monthsInclude(date) || startMonthIncludes(date) || endMonthIncludes(date);
        }
    }

    private boolean monthsInclude(LocalDate date) {
        int month = date.getMonthValue();
        return month > startMonth.getValue() && month < endMonth.getValue();
    }

    private boolean startMonthIncludes(LocalDate date) {
        if (date.getMonthValue() != startMonth.getValue()) {
            return false;
        } else if (startDay == 0) {
            return true;
        } else {
            return date.getDayOfMonth() >= startDay;
        }
    }

    private boolean endMonthIncludes(LocalDate date) {
        if (date.getMonthValue() != endMonth.getValue()) {
            return false;
        } else if (endDay == 0) {
            return true;
        } else {
            return date.getDayOfMonth() <= endDay;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RangeEveryYearTE)) {
            return false;
        }
        RangeEveryYearTE other = (RangeEveryYearTE) o;
        return endDay == other.endDay && endMonth == other.endMonth && startDay == other.startDay && startMonth == other.startMonth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + endDay;
        result = prime * result + ((endMonth == null) ? 0 : endMonth.hashCode());
        result = prime * result + startDay;
        result = prime * result + ((startMonth == null) ? 0 : startMonth.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
            .append('[')
            .append(this.getClass().getSimpleName())
            .append(": startMonth=")
            .append(startMonth)
            .append(" endMonth=")
            .append(endMonth)
            .append(" startDay=")
            .append(startDay)
            .append(" endDay=")
            .append(endDay)
            .append(']');
        return sb.toString();
    }
}
