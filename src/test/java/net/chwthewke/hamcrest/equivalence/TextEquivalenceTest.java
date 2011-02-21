package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_LEADING_WHITESPACE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_TRAILING_WHITESPACE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_WHITESPACE;
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

    @Test
    public void textEquivalenceIgnoringLeadingWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_LEADING_WHITESPACE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "  abc", "\tabc" ) );
        assertThat( textEquivalence, separates( "abc", " abc ", "a b c" ) );
    }

    @Test
    public void describeTextEquivalenceIgnoringLeadingWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_LEADING_WHITESPACE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "  abc" ) );
        // Verify
        assertThat( description, is( equalTo( "left-trimmed()=\"abc\"" ) ) );
    }

    @Test
    public void textEquivalenceIgnoringTrailingWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_TRAILING_WHITESPACE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "abc ", "abc\t" ) );
        assertThat( textEquivalence, separates( "abc", " abc", "a b c" ) );
    }

    @Test
    public void describeTextEquivalenceIgnoringTrailingWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_TRAILING_WHITESPACE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "abc  " ) );
        // Verify
        assertThat( description, is( equalTo( "right-trimmed()=\"abc\"" ) ) );
    }

    @Test
    public void textEquivalenceIgnoringExtremalWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_LEADING_WHITESPACE, IGNORE_TRAILING_WHITESPACE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", " abc ", "   abc\t" ) );
        assertThat( textEquivalence, separates( "abc", " ab c", "a b c" ) );
    }

    @Test
    public void describeTextEquivalenceIgnoringExtremalWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_LEADING_WHITESPACE, IGNORE_TRAILING_WHITESPACE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "  abc  " ) );
        // Verify
        assertThat( description, is( equalTo( "trimmed()=\"abc\"" ) ) );
    }

    @Test
    public void textEquivalenceIgnoringAllWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_WHITESPACE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "a bc ", "  abc\t" ) );
        assertThat( textEquivalence, separates( "abc", " aBc", "A b c" ) );
    }

    @Test
    public void describeTextEquivalenceIgnoringAllWhitespace( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_WHITESPACE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "a b c" ) );
        // Verify
        assertThat( description, is( equalTo( "ignoring whitespace()=\"abc\"" ) ) );
    }

    @Test
    public void textEquivalenceIgnoringAllWhitespaceAndCase( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_WHITESPACE, IGNORE_CASE );
        // Exercise

        // Verify
        assertThat( textEquivalence, equates( "abc", "a Bc ", "  Abc\t" ) );
        assertThat( textEquivalence, separates( "abc", " <Bc", "A b d" ) );
    }

    @Test
    public void describeTextEquivalenceIgnoringAllWhitespaceAndCase( ) throws Exception {
        // Setup
        textEquivalence = TextEquivalence.textEquivalenceWith( IGNORE_WHITESPACE, IGNORE_CASE );
        // Exercise
        final String description = describe( textEquivalence.equivalentTo( "A b c" ) );
        // Verify
        assertThat( description, is( equalTo( "ignoring whitespace()=equalToIgnoringCase(\"Abc\")" ) ) );
    }

}
