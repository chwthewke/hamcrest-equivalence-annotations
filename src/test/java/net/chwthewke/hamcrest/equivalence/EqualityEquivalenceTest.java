package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Sets.newHashSet;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static org.hamcrest.MatcherAssert.assertThat;

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
        assertThat( equivalence, separates( "a", "b" ) );
    }

    @Test
    public void equalitySupportsNulls( ) throws Exception {
        // Setup
        final EqualityEquivalence<String> equality = new EqualityEquivalence<String>( );
        // Exercise

        // Verify
        assertThat( equality, EquivalenceClassMatchers.<String>equates( null, null ) );
        assertThat( equality, EquivalenceClassMatchers.<String>separates( null, "ab" ) );
    }
}
