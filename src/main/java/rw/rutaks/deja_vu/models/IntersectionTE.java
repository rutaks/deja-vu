package rw.rutaks.deja_vu.models;

import rw.rutaks.deja_vu.interfaces.TemporalExpression;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public final class IntersectionTE extends CompositeTE {

    private IntersectionTE(List<TemporalExpression> expressions) {
        super(expressions);
    }

    public static IntersectionTE of(List<TemporalExpression> expressions) {
        return new IntersectionTE(expressions);
    }

    public static IntersectionTE of(TemporalExpression... expressions) {
        checkExpressionsForNull(expressions);
        return new IntersectionTE(Arrays.asList(expressions));
    }

    @Override
    public boolean includes(LocalDate date) {
        for (TemporalExpression e : expressions) {
            if (!e.includes(date)) {
                return false;
            }
        }
        return true;
    }
}
