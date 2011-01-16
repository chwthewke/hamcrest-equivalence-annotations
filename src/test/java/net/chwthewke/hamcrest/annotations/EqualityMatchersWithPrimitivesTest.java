package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class EqualityMatchersWithPrimitivesTest {

    @Test
    public void createMatcherOfPrimitiveProperties( ) throws Exception {
        // Setup
        // Exercise
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecification.class, Matched.class )
                    .equivalentTo( new Matched( 1, 4 ) );
        // Verify
        final Description description = new StringDescription( );
        matcher.describeTo( description );
        assertThat( description.toString( ), is( equalTo( "a Matched with getFirst()=<1>, getSecond()=<4>" ) ) );
    }

    @Test
    public void matchPrimitivePropertiesWithDifferentBoxes( ) throws Exception {
        // Setup
        final Matched original = new Matched( 1, 43210 );
        final Matched expected = new Matched( 1, 43210 );

        final Method m = Matched.class.getMethod( "getSecond" );
        assertThat( m.invoke( original ) == m.invoke( expected ), is( false ) );

        // Exercise
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecification.class, Matched.class )
                    .equivalentTo( original );
        // Verify

        assertThat( matcher.matches( expected ), is( true ) );
    }

    public static class Matched {
        public Matched( final int first, final int second ) {
            this.first = first;
            this.second = second;
        }

        public int getFirst( ) {
            return first;
        }

        public int getSecond( ) {
            return second;
        }

        private final int first;
        private final int second;
    }

    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        @Equality
        int getFirst( );

        @Identity
        int getSecond( );
    }

}
