package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers;

import org.junit.Ignore;
import org.junit.Test;

public class OnArrayElementsTest {

    @Test
    public void equivalenceInOrderIgnoringCase( ) throws Exception {
        // Setup
        final Equivalence<WithStringArray> equivalence = asSpecifiedBy(
            EqualIgnoringCaseInOrder.class, WithStringArray.class );
        // Exercise

        // Verify
        assertThat(
            equivalence,
            equates( new WithStringArray( "abc", "def" ), new WithStringArray( "abc", "def" ), new WithStringArray(
                "ABC", "DeF" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new WithStringArray( "abc", "def" ), new WithStringArray( "def", "abc" ),
                new WithStringArray( "ABC" ), new WithStringArray( "abc", "deg" ) ) );
    }

    @Test
    public void equivalenceInAnyOrder( ) throws Exception {
        // Setup
        final Equivalence<WithStringArray> equivalence = asSpecifiedBy( EqualInAnyOrder.class, WithStringArray.class );
        // Exercise

        // Verify
        assertThat( equivalence,
            equates( new WithStringArray( "abc", "def" ), new WithStringArray( "def", "abc" ) ) );
        assertThat( equivalence,
            EquivalenceClassMatchers.separates(
                new WithStringArray( "abc", "def" ), new WithStringArray( "ABC", "def" ),
                new WithStringArray( "DEF", "abc" ), new WithStringArray( "abc", "deg" ) ) );
    }

    @Ignore
    @Test
    public void approxEqualityInOrderOnPrimitives( ) throws Exception {
        // Setup
        final Equivalence<WithPrimitiveArray> equivalence =
                asSpecifiedBy( ApproximatelyEqualInOrder.class, WithPrimitiveArray.class );
        // Exercise

        // Verify
        assertThat( equivalence, equates(
            new WithPrimitiveArray( 0d, 0.1d, -5d ),
            new WithPrimitiveArray( 0d, 0.10001d, -5d ),
            new WithPrimitiveArray( 0d, 0.1d, -4.99999d )
            ) );
    }

    public static class WithStringArray {
        public WithStringArray( final String... words ) {
            this.words = words;
        }

        public String[ ] getWords( ) {
            return words;
        }

        @Override
        public String toString( ) {
            return String.format( "Matched [words=%s]", Arrays.toString( words ) );
        }

        private final String[ ] words;
    }

    @EquivalenceSpecificationOn( WithStringArray.class )
    public static interface EqualIgnoringCaseInOrder {
        @Text( options = IGNORE_CASE )
        @OnArrayElements( )
        String[ ] getWords( );
    }

    @EquivalenceSpecificationOn( WithStringArray.class )
    public static interface EqualInAnyOrder {
        @Text
        @OnArrayElements( inOrder = false )
        String[ ] getWords( );
    }

    public static class WithPrimitiveArray {

        public WithPrimitiveArray( final double... values ) {
            this.values = values;
        }

        public double[ ] getValues( ) {
            return values;
        }

        @Override
        public String toString( ) {
            return String.format( "WithPrimitiveArray [values=%s]", Arrays.toString( values ) );
        }

        private final double[ ] values;
    }

    @EquivalenceSpecificationOn( WithPrimitiveArray.class )
    public static interface ApproximatelyEqualInOrder {
        @OnArrayElements
        @ApproximateEquality( tolerance = 0.001d )
        double[ ] getValues( );
    }

}
