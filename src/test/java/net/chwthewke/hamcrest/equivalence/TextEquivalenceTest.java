package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class TextEquivalenceTest {

    private Equivalence<String> textEquivalence;

    @Test
    public void exactEquivalenceEquatesEqualStringsOnly( ) throws Exception {
        // Setup
        textEquivalence = new TextEquivalence( );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "abc" ) );
        assertThat( textEquivalence, separates( "abc", "ABC", "a b c" ) );
    }

}
