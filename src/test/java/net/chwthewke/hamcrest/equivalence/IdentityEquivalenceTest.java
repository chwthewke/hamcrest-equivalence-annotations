package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

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
        assertThat( equivalence, not( equates( v1, v2 ) ) );
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