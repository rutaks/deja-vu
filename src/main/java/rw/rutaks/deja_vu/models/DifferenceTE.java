package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.LocalDate;

public final class DifferenceTE implements TemporalExpression {

    /**
     * Included sub-expression
     */
    private final TemporalExpression included;

    /**
     * Excluded sub-expression
     */
    private final TemporalExpression excluded;

    private DifferenceTE(TemporalExpression included, TemporalExpression excluded) {
        this.included = included;
        this.excluded = excluded;
    }

    public static DifferenceTE of(TemporalExpression included, TemporalExpression excluded) {
        return new DifferenceTE(included, excluded);
    }

    @Override
    public boolean includes(LocalDate date) {
        return included.includes(date) && !excluded.includes(date);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
            .append('[')
            .append(this.getClass().getSimpleName())
            .append(": included=")
            .append(included)
            .append(", excluded=")
            .append(excluded)
            .append(']');
        return sb.toString();
    }
}
