package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import org.junit.Ignore;
import org.junit.Test;

public class ApproximateEqualityEquivalenceTest {
    @Test
    public void equatesCloseValues( ) throws Exception {
        // Setup
        final Equivalence<Double> equivalence = new ApproximateEqualityEquivalence( 0.000001d );
        // Exercise
        // Verify
        assertThat( equivalence, equates( 15d, 14.999999d ) );
    }

    @Test
    public void differentiatesValuesNotCloseEnough( ) throws Exception {
        // Setup
        final Equivalence<Double> equivalence = new ApproximateEqualityEquivalence( 0.000001d );
        // Exercise
        // Verify
        assertThat( equivalence, not( equates( 15d, 14.999998d ) ) );
    }

    @Test( expected = IllegalArgumentException.class )
    @Ignore( "TODO" )
    public void throwsWithNegativeTolerance( ) throws Exception {
        // Setup
        // Exercise
        new ApproximateEqualityEquivalence( -0.000001d );
        // Verify
    }
}
