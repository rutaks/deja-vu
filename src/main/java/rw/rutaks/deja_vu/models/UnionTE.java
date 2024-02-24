package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public final class UnionTE extends CompositeTE {

    private UnionTE(List<TemporalExpression> expressions) {
        super(expressions);
    }

    public static UnionTE of(List<TemporalExpression> expressions) {
        return new UnionTE(expressions);
    }

    public static UnionTE of(TemporalExpression... expressions) {
        checkExpressionsForNull(expressions);
        return new UnionTE(Arrays.asList(expressions));
    }

    @Override
    public boolean includes(LocalDate date) {
        for (TemporalExpression e : expressions) {
            if (e.includes(date)) {
                return true;
            }
        }
        return false;
    }
}
