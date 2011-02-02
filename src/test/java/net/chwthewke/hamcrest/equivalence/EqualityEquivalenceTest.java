package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Sets.newHashSet;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

import java.util.Collection;

import org.junit.Test;

public class EqualityEquivalenceTest {

    @Test
    public void equatesEqualNumbers( ) throws Exception {
        // Setup
        final Long left = new Long( 4512789630L );
        final Long right = new Long( 4512789630L );

        final Equivalence<Long> equality = new EqualityEquivalence<Long>( );

        // Exercise
        // Verify
        assertThat( equality, equates( left, right ) );
    }

    @Test
    public void equatesEqualCollections( ) throws Exception {
        // Setup
        final Collection<String> left = newHashSet( "a", "b" );
        final Collection<String> right = newHashSet( "b", "a" );

        final Equivalence<Collection<String>> equality = new EqualityEquivalence<Collection<String>>( );

        // Exercise
        // Verify
        assertThat( equality, equates( left, right ) );
    }

    @Test
    public void differentiatesDifferentStrings( ) throws Exception {
        // Setup
        final EqualityEquivalence<String> equivalence = new EqualityEquivalence<String>( );
        // Exercise

        // Verify
        assertThat( equivalence, not( equates( "a", "b" ) ) );
    }
}
