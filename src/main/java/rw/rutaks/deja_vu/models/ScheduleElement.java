package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.LocalDate;

public final class ScheduleElement {

    /**
     * String representing a number of slots for the event
     */
    private final int slots;

    /**
     * An expression describing the recurrence of an event
     */
    private final TemporalExpression expression;

    private ScheduleElement(TemporalExpression expression, int slots) {
        this.expression = expression;
        this.slots = slots;
    }

    public static ScheduleElement of(TemporalExpression expression, int slots) {
        return new ScheduleElement(expression, slots);
    }

    public boolean isOccurring(LocalDate date) {
        return expression.includes(date);
    }

    public int slots() {
        return slots;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.getClass().getSimpleName()).append(": event='").append("' expression=").append(expression).append(']');
        return sb.toString();
    }
}
