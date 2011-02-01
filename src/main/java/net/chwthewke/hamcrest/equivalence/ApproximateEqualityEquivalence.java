package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.closeTo;

import org.hamcrest.Matcher;

public final class ApproximateEqualityEquivalence implements Equivalence<Double> {

    public ApproximateEqualityEquivalence( final double tolerance ) {
        this.tolerance = tolerance;
    }

    public Matcher<Double> equivalentTo( final Double expected ) {
        return closeTo( expected, tolerance );
    }

    private final double tolerance;
}
