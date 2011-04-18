package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ApproximateEqualityEquivalenceTest {
    @Test
    public void equatesCloseValues( ) throws Exception {
        // Setup
        final Equivalence<Number> equivalence = new ApproximateEqualityEquivalence( 0.000001d );
        // Exercise
        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<Number>equates( 15d, 14.999999d ) );
    }

    @Test
    public void differentiatesValuesNotCloseEnough( ) throws Exception {
        // Setup
        final Equivalence<Number> equivalence = new ApproximateEqualityEquivalence( 0.000001d );
        // Exercise
        // Verify
        assertThat( equivalence,
            EquivalenceClassMatchers.<Number>separates( 15d, 14.999998d ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void throwsWithNegativeTolerance( ) throws Exception {
        // Setup
        // Exercise
        new ApproximateEqualityEquivalence( -0.000001d );
        // Verify
    }
}
