package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.annotations.CompositeMatcherFactory.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.declarations.ReflectivePrimitiveMatch;
import net.chwthewke.hamcrest.annotations.declarations.ReflectivePrimitiveMatch.Matched;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class MatchPrimitivePropertiesTest {

    @Test
    public void createMatcherOfPrimitiveProperties( ) throws Exception {
        // Setup
        // Exercise
        final Matcher<Matched> matcher = asSpecifiedBy(
            ReflectivePrimitiveMatch.MatcherSpecification.class, ReflectivePrimitiveMatch.Matched.class )
            .equivalentTo( new ReflectivePrimitiveMatch.Matched( 1, 4 ) );
        // Verify
        final Description description = new StringDescription( );
        matcher.describeTo( description );
        assertThat( description.toString( ), is( equalTo( "a Matched with getFirst()=<1>, getSecond()=<4>" ) ) );
    }

    @Test
    public void matchPrimitivePropertiesWithDifferentBoxes( ) throws Exception {
        // Setup
        final ReflectivePrimitiveMatch.Matched original = new ReflectivePrimitiveMatch.Matched( 1, 43210 );
        final ReflectivePrimitiveMatch.Matched expected = new ReflectivePrimitiveMatch.Matched( 1, 43210 );

        final Method m = ReflectivePrimitiveMatch.Matched.class.getMethod( "getSecond" );
        assertThat( m.invoke( original ) == m.invoke( expected ), is( false ) );

        // Exercise
        final Matcher<Matched> matcher = asSpecifiedBy(
            ReflectivePrimitiveMatch.MatcherSpecification.class, ReflectivePrimitiveMatch.Matched.class )
            .equivalentTo( original );
        // Verify

        assertThat( matcher.matches( expected ), is( true ) );
    }

}
