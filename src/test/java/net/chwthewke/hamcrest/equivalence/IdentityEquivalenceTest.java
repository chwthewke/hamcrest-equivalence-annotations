package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class IdentityEquivalenceTest {

    @Test
    public void equatesWithSameReference( ) throws Exception {
        // Setup
        final V v = new V( 42 );
        final IdentityEquivalence<V> equivalence = new IdentityEquivalence<V>( );
        // Exercise
        // Verify
        assertThat( equivalence, equates( v, v ) );
    }

    @Test
    public void differentiatesJustEqualObjects( ) throws Exception {
        // Setup
        final V v1 = new V( 42 );
        final V v2 = new V( 42 );
        final IdentityEquivalence<V> equivalence = new IdentityEquivalence<V>( );
        // Exercise
        // Verify
        assertThat( equivalence, separates( v1, v2 ) );
    }

    @Test
    public void identitySupportsNulls( ) throws Exception {
        // Setup
        // Exercise
        final IdentityEquivalence<V> equivalence = new IdentityEquivalence<V>( );

        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<V>equates( null, null ) );
        assertThat( equivalence, EquivalenceClassMatchers.<V>separates( null, new V( 12 ) ) );
    }

    private static class V {

        public V( final int value ) {
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
            final V other = (V) obj;
            if ( value != other.value )
                return false;
            return true;
        }

        private final int value;
    }
}
