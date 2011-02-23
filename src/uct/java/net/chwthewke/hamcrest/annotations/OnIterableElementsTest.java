package net.chwthewke.hamcrest.annotations;

import static com.google.common.collect.Lists.newArrayList;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers;

import org.junit.Test;

public class OnIterableElementsTest {

    @Test
    public void equivalenceInOrderIgnoringCase( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence = asSpecifiedBy( EqualIgnoringCaseInOrder.class, Matched.class );
        // Exercise

        // Verify
        assertThat( equivalence,
            equates( new Matched( "abc", "def" ), new Matched( "abc", "def" ), new Matched( "ABC", "DeF" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new Matched( "abc", "def" ), new Matched( "def", "abc" ),
                new Matched( "ABC" ), new Matched( "abc", "deg" ) ) );
    }

    @Test
    public void equivalenceInAnyOrder( ) throws Exception {
        // Setup
        final Equivalence<Matched> equivalence = asSpecifiedBy( EqualInAnyOrder.class, Matched.class );
        // Exercise

        // Verify
        assertThat( equivalence,
            equates( new Matched( "abc", "def" ), new Matched( "def", "abc" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new Matched( "abc", "def" ), new Matched( "ABC", "def" ),
                new Matched( "DEF", "abc" ), new Matched( "abc", "deg" ) ) );
    }

    public static class Matched {
        public Matched( final String... words ) {
            this.words = newArrayList( words );
        }

        public Collection<String> getWords( ) {
            return words;
        }

        @Override
        public String toString( ) {
            return String.format( "Matched [words=%s]", words );
        }

        private final Collection<String> words;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface EqualIgnoringCaseInOrder {
        @Text( options = IGNORE_CASE )
        @OnIterableElements( elementType = String.class )
        Collection<String> getWords( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface EqualInAnyOrder {
        @Text
        @OnIterableElements( elementType = String.class, inOrder = false )
        Collection<String> getWords( );
    }

}
