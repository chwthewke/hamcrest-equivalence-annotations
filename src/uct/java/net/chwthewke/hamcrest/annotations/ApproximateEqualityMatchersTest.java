package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.matchers.EquivalenceSpecification;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ApproximateEqualityMatchersTest {

    @Test
    public void matchApproximateEqualityOnDouble( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( DoubleMatcherSpecification.class )
                    .equivalentTo( new Matched( 1.25d, 0f ) );
        final Matched actual = new Matched( 1.2500005d, 0f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }

    @Test
    public void matchApproximateEqualityOnFloat( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( FloatMatcherSpecification.class )
                    .equivalentTo( new Matched( 0d, 1.1f ) );
        final Matched actual = new Matched( 0d, 1.0999999f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }

    public static class Matched {

        Matched( final double doubleValue, final float floatValue ) {
            this.doubleValue = doubleValue;
            this.floatValue = floatValue;
        }

        public double getDoubleValue( ) {
            return doubleValue;
        }

        public float getFloatValue( ) {
            return floatValue;
        }

        private final double doubleValue;
        private final float floatValue;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface DoubleMatcherSpecification extends EquivalenceSpecification<Matched> {
        @ApproximateEquality( tolerance = 0.000001d )
        double getDoubleValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface FloatMatcherSpecification extends EquivalenceSpecification<Matched> {
        @ApproximateEquality( tolerance = 0.000001d )
        float getFloatValue( );
    }
}
