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
        final Equivalence<WithStringCollection> equivalence = asSpecifiedBy( EqualIgnoringCaseInOrder.class, WithStringCollection.class );
        // Exercise

        // Verify
        assertThat( equivalence,
            equates( new WithStringCollection( "abc", "def" ), new WithStringCollection( "abc", "def" ), new WithStringCollection( "ABC", "DeF" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new WithStringCollection( "abc", "def" ), new WithStringCollection( "def", "abc" ),
                new WithStringCollection( "ABC" ), new WithStringCollection( "abc", "deg" ) ) );
    }

    @Test
    public void equivalenceInAnyOrder( ) throws Exception {
        // Setup
        final Equivalence<WithStringCollection> equivalence = asSpecifiedBy( EqualInAnyOrder.class, WithStringCollection.class );
        // Exercise

        // Verify
        assertThat( equivalence,
            equates( new WithStringCollection( "abc", "def" ), new WithStringCollection( "def", "abc" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new WithStringCollection( "abc", "def" ), new WithStringCollection( "ABC", "def" ),
                new WithStringCollection( "DEF", "abc" ), new WithStringCollection( "abc", "deg" ) ) );
    }

    public static class WithStringCollection {
        public WithStringCollection( final String... words ) {
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

    @EquivalenceSpecificationOn( WithStringCollection.class )
    public static interface EqualIgnoringCaseInOrder {
        @Text( options = IGNORE_CASE )
        @OnIterableElements( elementType = String.class )
        Collection<String> getWords( );
    }

    @EquivalenceSpecificationOn( WithStringCollection.class )
    public static interface EqualInAnyOrder {
        @Text
        @OnIterableElements( elementType = String.class, inOrder = false )
        Collection<String> getWords( );
    }

}
