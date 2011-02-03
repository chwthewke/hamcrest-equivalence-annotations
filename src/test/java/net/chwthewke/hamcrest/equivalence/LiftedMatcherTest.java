package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPublicProperty;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.common.base.Function;

public class LiftedMatcherTest {
    @Test
    public void matchIsPerformedOnProjectedValue( ) throws Exception {
        final Matcher<WithPublicProperty> liftedMatcher =
                new LiftedMatcher<WithPublicProperty, String>( "getValue", GET_VALUE, equalTo( "text" ) );
        // Exercise
        // Verify
        assertThat( liftedMatcher.matches( new WithPublicProperty( "text" ) ), is( true ) );
    }

    @Test
    public void describeWithProjection( ) throws Exception {
        // Setup
        final Matcher<WithPublicProperty> liftedMatcher =
                new LiftedMatcher<WithPublicProperty, String>( "getValue", GET_VALUE, equalTo( "text" ) );

        final Description description = new StringDescription( );
        // Exercise
        liftedMatcher.describeTo( description );
        // Verify
        assertThat( description.toString( ), is( "getValue()=\"text\"" ) );
    }

    @Test
    public void mismatchDescribesProjection( ) throws Exception {
        // Setup
        final Matcher<WithPublicProperty> liftedMatcher =
                new LiftedMatcher<WithPublicProperty, String>( "getValue", GET_VALUE, equalTo( "text" ) );
        final StringDescription mismatchDescription = new StringDescription( );
        final WithPublicProperty actual = new WithPublicProperty( "tax" );
        // Exercise
        final boolean match = liftedMatcher.matches( actual );
        liftedMatcher.describeMismatch( actual, mismatchDescription );
        // Verify
        assertThat( match, is( false ) );
        assertThat( mismatchDescription.toString( ), is( equalTo( "getValue() was \"tax\"" ) ) );
    }

    private static final Function<WithPublicProperty, String> GET_VALUE = new Function<WithPublicProperty, String>( ) {
        public String apply( final WithPublicProperty input ) {
            return input.getValue( );
        }
    };
}
