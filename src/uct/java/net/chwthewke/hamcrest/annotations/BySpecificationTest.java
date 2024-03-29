package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.matchers.EquivalenceSpecification;

import org.hamcrest.Matcher;
import org.junit.Test;

public class BySpecificationTest {

    @Test
    public void matchBySpecificationOnProperty( ) throws Exception {
        // Setup
        final OuterMatched expected = new OuterMatched( new InnerMatched( "abcd" ) );
        final OuterMatched actual = new OuterMatched( new InnerMatched( "abcd" ) );
        final Matcher<OuterMatched> matcher = asSpecifiedBy( OuterSpecification.class )
            .equivalentTo( expected );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void mismatchBySpecificationOnProperty( ) throws Exception {
        // Setup
        final OuterMatched expected = new OuterMatched( new InnerMatched( "abcd" ) );
        final OuterMatched actual = new OuterMatched( new InnerMatched( "abcde" ) );
        final Matcher<OuterMatched> matcher = asSpecifiedBy( OuterSpecification.class )
            .equivalentTo( expected );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( false ) );
        assertThat( describeMismatch( matcher, actual ),
            is( equalTo( "getOuterValue() getInnerValue() was \"abcde\"" ) ) );
    }

    @EquivalenceSpecificationOn( OuterMatched.class )
    public static interface OuterSpecification extends EquivalenceSpecification<OuterMatched> {
        @BySpecification( InnerSpecification.class )
        InnerMatched getOuterValue( );
    }

    public static class OuterMatched {
        public OuterMatched( final InnerMatched value ) {
            this.value = value;
        }

        public InnerMatched getOuterValue( ) {
            return value;
        }

        private final InnerMatched value;
    }

    @EquivalenceSpecificationOn( InnerMatched.class )
    public static interface InnerSpecification extends EquivalenceSpecification<InnerMatched> {
        @Equality
        String getInnerValue( );
    }

    public static class InnerMatched {
        public InnerMatched( final String value ) {
            this.value = value;
        }

        public String getInnerValue( ) {
            return value;
        }

        private final String value;
    }
}
