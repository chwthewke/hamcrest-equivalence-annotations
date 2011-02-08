package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_LEADING_WHITESPACE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class TextEquivalenceTest {

    private Equivalence<String> textEquivalence;

    @Test
    public void exactEquivalenceEquatesEqualStringsOnly( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "abc" ) );
        assertThat( textEquivalence, separates( "abc", "ABC", "a b c" ) );
    }

    @Test
    public void exactEquivalenceDescription( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "abc" ) );
        // Verify
        assertThat( description, is( equalTo( "\"abc\"" ) ) );
    }

    @Test
    public void textEquivalenceIgnoringCase( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_CASE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "ABC", "AbC" ) );
        assertThat( textEquivalence, equates( "é", "É" ) );
        assertThat( textEquivalence, separates( "abc", "AB C", "a b c" ) );
    }

    @Test
    public void textEquivalenceIgnoringCaseDescription( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_CASE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "abc" ) );
        // Verify
        assertThat( description, is( equalTo( "equalToIgnoringCase(\"abc\")" ) ) );
    }

    // TODO also test matcher descriptions

    @Test
    public void textEquivalenceIgnoringLeadingWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_LEADING_WHITESPACE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "  abc", "\tabc" ) );
        assertThat( textEquivalence, separates( "abc", " abc ", "a b c" ) );
    }

}
