package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.MatcherUtils;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;
import org.junit.Test;

public class WidenedEquivalenceTest {

    @Test
    public void matcherOfSupertypeHasSpecificDescription( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence =
                asSpecifiedBy( MatchingSpecification.class, MatchedImpl.class, Matched.class );

        final Matcher<Matched> matcherOnSupertype =
                equivalence.equivalentTo( new MatchedImpl( new IntHolder( 4 ) ) );
        // Exercise
        final String description = MatcherUtils.describe( matcherOnSupertype );
        // Verify
        assertThat( description, startsWith( "a MatchedImpl with getHolder()=" +
                "<net.chwthewke.hamcrest.matchers.WidenedEquivalenceTest$IntHolder@" ) );
    }

    @Test
    public void matcherOfSupertypeMatchesValidInstance( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence =
                asSpecifiedBy( MatchingSpecification.class, MatchedImpl.class, Matched.class );

        final Matcher<Matched> matcherOnSupertype =
                equivalence.equivalentTo( new MatchedImpl( new IntHolder( 4 ) ) );
        // Exercise
        final boolean match = matcherOnSupertype.matches( new MatchedImpl( new IntHolder( 4 ) ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void matcherOfSupertypeDoesNotMatchInvalidInstance( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence =
                asSpecifiedBy( MatchingSpecification.class, MatchedImpl.class, Matched.class );

        final Matcher<Matched> matcherOnSupertype =
                equivalence.equivalentTo( new MatchedImpl( new IntHolder( 4 ) ) );
        final MatchedImpl actual = new MatchedImpl( new IntHolder( 6 ) );
        // Exercise
        final boolean match = matcherOnSupertype.matches( actual );
        // Verify
        assertThat( match, is( false ) );
        assertThat( describeMismatch( matcherOnSupertype, actual ),
            startsWith( "getHolder() was <net.chwthewke.hamcrest.matchers.WidenedEquivalenceTest$IntHolder@" ) );
    }

    @Test
    public void matcherOfSupertypeFailsOnActualOfWrongType( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence =
                asSpecifiedBy( MatchingSpecification.class, MatchedImpl.class, Matched.class );

        final Matcher<Matched> matcherOnSupertype =
                equivalence.equivalentTo( new MatchedImpl( new IntHolder( 4 ) ) );
        final OtherMatchedImpl actual = new OtherMatchedImpl( );
        // Exercise
        final boolean match = matcherOnSupertype.matches( actual );
        // Verify
        assertThat( match, is( false ) );
        assertThat( describeMismatch( matcherOnSupertype, actual ),
            is( equalTo( "was an instance of the incompatible type " +
                    "\"net.chwthewke.hamcrest.matchers.WidenedEquivalenceTest$OtherMatchedImpl\"" ) ) );
    }

    @Test
    public void equivalenceOfSupertypeCannotProduceMatcherFromExpectedOfWrongType( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence =
                asSpecifiedBy( MatchingSpecification.class, MatchedImpl.class, Matched.class );

        // Exercise
        try
        {
            equivalence.equivalentTo( new OtherMatchedImpl( ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "This equivalence can only produce matchers " +
                    "for members of type net.chwthewke.hamcrest.matchers." +
                    "WidenedEquivalenceTest$MatchedImpl." ) ) );
        }

    }

    public static interface Matched {
        IntHolder getHolder( );
    }

    public static class MatchedImpl implements Matched {

        public MatchedImpl( final IntHolder holder ) {
            this.holder = holder;
        }

        public IntHolder getHolder( ) {
            return holder;
        }

        private final IntHolder holder;
    }

    public static class OtherMatchedImpl implements Matched {

        public IntHolder getHolder( ) {
            return null;
        }

    }

    @EquivalenceSpecificationOn( MatchedImpl.class )
    public static interface MatchingSpecification extends EquivalenceSpecification<MatchedImpl> {
        @Equality
        IntHolder getHolder( );
    }

    public static class IntHolder {
        public IntHolder( final int value ) {
            this.value = value;
        }

        @Override
        public int hashCode( ) {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals( final Object obj ) {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass( ) != obj.getClass( ) )
                return false;
            final IntHolder other = (IntHolder) obj;
            if ( value != other.value )
                return false;
            return true;
        }

        private final int value;
    }

}
