package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.hamcrest.Matcher;
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

        // Exercise
        // Verify
        assertThat( describe( liftedMatcher ), is( "getValue()=\"text\"" ) );
    }

    @Test
    public void mismatchDescribesProjection( ) throws Exception {
        // Setup
        final Matcher<WithPublicProperty> liftedMatcher =
                new LiftedMatcher<WithPublicProperty, String>( "getValue", GET_VALUE, equalTo( "text" ) );
        final WithPublicProperty actual = new WithPublicProperty( "tax" );
        // Exercise
        final boolean match = liftedMatcher.matches( actual );
        final String mismatchDescription = describeMismatch( liftedMatcher, actual );
        // Verify
        assertThat( match, is( false ) );
        assertThat( mismatchDescription, is( equalTo( "getValue() was \"tax\"" ) ) );
    }

    private static final Function<WithPublicProperty, String> GET_VALUE = new Function<WithPublicProperty, String>( ) {
        public String apply( final WithPublicProperty input ) {
            return input.getValue( );
        }
    };
}
