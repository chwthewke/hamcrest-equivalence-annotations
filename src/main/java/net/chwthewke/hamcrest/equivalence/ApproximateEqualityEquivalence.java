package net.chwthewke.hamcrest.equivalence;

import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.closeTo;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

/**
 * The {@link EqualityEquivalence} class defines equivalence on anynumeric type as close equality up to a given tolerance. For this purpose
 * a numeric type is considered to be {@link Number}, any of its subclasses or their primitive counterparts where applicable.
 */
public final class ApproximateEqualityEquivalence implements Equivalence<Number> {

    /**
     * Creates an {@link ApproximateEqualityEquivalence} with the given tolerance.
     * 
     * @param tolerance
     *            The tolerance.
     */
    public ApproximateEqualityEquivalence( final double tolerance ) {
        checkArgument( tolerance > 0, "'tolerance' must be strictly positive." );
        this.tolerance = tolerance;
    }

    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An object of the acted upon type.
     * @return A {@link Matcher} that matches objects equivalent to <code>expected</code>.
     */
    public Matcher<Number> equivalentTo( final Number expected ) {
        return new LiftedMatcher<Number, Double>( "", DOUBLE_VALUE, closeTo( expected.doubleValue( ), tolerance ) );
    }

    private final double tolerance;

    private static final Function<Number, Double> DOUBLE_VALUE =
            new Function<Number, Double>( ) {
                public Double apply( final Number input ) {
                    return input.doubleValue( );
                }
            };
}
