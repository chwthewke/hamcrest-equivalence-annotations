package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;
import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static net.chwthewke.hamcrest.equivalence.IterableEquivalences.iterableEquivalence;
import static net.chwthewke.hamcrest.equivalence.IterableEquivalences.iterableEquivalenceInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.junit.Test;

public class IterablesEquivalenceTest {
    @SuppressWarnings( "unchecked" )
    @Test
    public void inOrderStringEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalence( componentEquivalence );
        // Verify
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
                newArrayList( "abc", "def" ),
                newArrayList( "ABC", "Def" ) ) );
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>separates(
                newArrayList( "abc", "def" ),
                newArrayList( "abc", "de f" ),
                newArrayList( "Def", "ABC" ) ) );
    }

    @Test
    public void describeInOrderStringEquivalenceMatcher( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalence( componentEquivalence );
        // Verify
        final String description = describe( equivalence.equivalentTo( newArrayList( "Abc", "def" ) ) );
        assertThat( description, is( equalTo( "iterable containing " +
                "[equalToIgnoringCase(\"Abc\"), equalToIgnoringCase(\"def\")]" ) ) );
    }

    @Test
    public void inOrderStringEquivalenceMismatch( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalence( componentEquivalence );
        final Matcher<Iterable<? extends String>> matcher = equivalence.equivalentTo( newArrayList( "ABC", "Def" ) );
        // Verify
        final String mismatchDescription = describeMismatch( matcher, newArrayList( "ABC", "D ef" ) );
        assertThat( mismatchDescription, is( equalTo( "item 1: was D ef" ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void inAnyOrderStringEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalenceInAnyOrder( componentEquivalence );
        // Verify
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
                newArrayList( "abc", "def" ),
                newArrayList( "ABC", "Def" ),
                newArrayList( "Def", "ABC" ) ) );
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>separates(
                newArrayList( "abc", "def" ),
                newArrayList( "abc", "de f" ) ) );
    }

    @Test
    public void describeInAnyOrderStringEquivalenceMatcher( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalenceInAnyOrder( componentEquivalence );
        // Verify
        final String description = describe( equivalence.equivalentTo( newArrayList( "Abc", "def" ) ) );
        assertThat( description, is( equalTo( "iterable over " +
                "[equalToIgnoringCase(\"Abc\"), equalToIgnoringCase(\"def\")] " +
                "in any order" ) ) );
    }

    @Test
    public void inAnyOrderStringEquivalenceMismatch( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = iterableEquivalenceInAnyOrder( componentEquivalence );
        final Matcher<Iterable<? extends String>> matcher = equivalence.equivalentTo( newArrayList( "ABC", "Def" ) );
        // Verify
        final String mismatchDescription = describeMismatch( matcher, newArrayList( "Def", "AB C" ) );
        assertThat( mismatchDescription, is( equalTo( "Not matched: \"AB C\"" ) ) );
    }

}