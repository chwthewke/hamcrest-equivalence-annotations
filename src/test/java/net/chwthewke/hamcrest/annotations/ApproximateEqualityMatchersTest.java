package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.declarations.FloatingPropertyMatch;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ApproximateEqualityMatchersTest {

    @Test
    public void matchApproximateEqualityOnDouble( ) throws Exception {
        // Setup
        final Matcher<FloatingPropertyMatch.Matched> matcher = CompositeMatcherFactory
            .asSpecifiedBy( FloatingPropertyMatch.DoubleMatcherSpecification.class )
            .equivalentTo( new FloatingPropertyMatch.Matched( 1.25d, 0f ) );
        final FloatingPropertyMatch.Matched actual = new FloatingPropertyMatch.Matched( 1.2500005d, 0f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }

    @Test
    public void matchApproximateEqualityOnFloat( ) throws Exception {
        // Setup
        final Matcher<FloatingPropertyMatch.Matched> matcher = CompositeMatcherFactory
            .asSpecifiedBy( FloatingPropertyMatch.FloatMatcherSpecification.class )
            .equivalentTo( new FloatingPropertyMatch.Matched( 0d, 1.1f ) );
        final FloatingPropertyMatch.Matched actual = new FloatingPropertyMatch.Matched( 0d, 1.0999999f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }
}
