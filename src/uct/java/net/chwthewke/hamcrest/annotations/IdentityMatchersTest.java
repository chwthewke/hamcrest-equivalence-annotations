package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.matchers.EquivalenceSpecification;

import org.junit.Test;

public class IdentityMatchersTest {

    @Test
    public void identicalReferencesMatch( ) throws Exception {
        // Setup
        final IntHolder ref = new IntHolder( 12 );
        final Matched first = new Matched( ref );
        final Matched second = new Matched( ref );
        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void equalButNotIdenticalReferencesDoNotMatch( ) throws Exception {
        // Setup
        final Matched first = new Matched( new IntHolder( 12 ) );
        final Matched second = new Matched( new IntHolder( 12 ) );

        assertThat( first.getHolder( ).equals( second.getHolder( ) ), is( true ) );

        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( false ) );
    }

    public static class Matched {

        public Matched( final IntHolder holder ) {
            this.holder = holder;
        }

        public IntHolder getHolder( ) {
            return holder;
        }

        private final IntHolder holder;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface MatchingSpecification extends EquivalenceSpecification<Matched> {
        @Identity
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
