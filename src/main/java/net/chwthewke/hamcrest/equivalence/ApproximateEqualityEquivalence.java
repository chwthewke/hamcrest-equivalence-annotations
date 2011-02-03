package net.chwthewke.hamcrest.equivalence;

import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.closeTo;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public final class ApproximateEqualityEquivalence implements Equivalence<Number> {

    public ApproximateEqualityEquivalence( final double tolerance ) {
        checkArgument( tolerance > 0, "'tolerance' must be strictly positive." );
        this.tolerance = tolerance;
    }

    public Matcher<Number> equivalentTo( final Number expected ) {
        return new LiftedMatcher<Number, Double>( "", DOUBLE_VALUE, closeTo( expected.doubleValue( ), tolerance ) );
    }

    private final double tolerance;

    private static final Function<Number, Double> DOUBLE_VALUE =
            new Function<Number, Double>( ) {
                public Double apply( final Number input ) {
                    return input == null ? null : input.doubleValue( );
                }
            };
}
