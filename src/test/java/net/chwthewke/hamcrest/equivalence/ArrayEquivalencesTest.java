package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forArrays;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.IterableEquivalences.iterableEquivalence;
import static net.chwthewke.hamcrest.equivalence.TextEquivalence.textEquivalenceWith;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ArrayEquivalencesTest {

    @Test
    public void testStringArrayInOrderEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String[ ]> equivalence =
                forArrays( iterableEquivalence( textEquivalenceWith( IGNORE_CASE ) ) );
        // Exercise
        // Verify
        assertThat( equivalence,
            equates( new String[ ] { "abc", "def" }, new String[ ] { "ABC", "DEF" } ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.<String[ ]>separates(
                new String[ ] { "abc", "def" }, new String[ ] { "ABC" }, new String[ ] { "ABC", "deg" } ) );
    }

}
