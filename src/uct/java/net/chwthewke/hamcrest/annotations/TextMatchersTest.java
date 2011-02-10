package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_LEADING_WHITESPACE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_TRAILING_WHITESPACE;
import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.junit.Test;

public class TextMatchersTest {

    @Test
    public void matchWithTextEquivalenceWithCaseOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseOptionOnly.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "Def" ) );
        // Exercise
        final boolean match = matcher.matches( new Matched( "Abc", "def" ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void describeTextEquivalenceWithCaseOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseOptionOnly.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "Def" ) );
        // Exercise
        final String description = describe( matcher );
        // Verify
        assertThat( description, is( equalTo( "a Matched with getFirst()=\"Abc\", " +
                "getSecond()=equalToIgnoringCase(\"Def\")" ) ) );
    }

    @Test
    public void describeTextEquivalenceMismatchWithCaseOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseOptionOnly.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "Def" ) );
        final Matched actual = new Matched( "Abc", "ABC" );
        // Exercise
        final String description = describeMismatch( matcher, actual );
        // Verify
        assertThat( matcher.matches( actual ), is( false ) );
        assertThat( description, is( equalTo( "getSecond() was ABC" ) ) );
    }

    @Test
    public void matchWithTextEquivalenceWithCaseAndTrimOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseAndTrim.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "\tDef " ) );
        // Exercise
        final boolean match = matcher.matches( new Matched( "abc", " def " ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void describeTextEquivalenceWithCaseAndTrimOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseAndTrim.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "\tDef " ) );
        // Exercise
        final String description = describe( matcher );
        // Verify
        assertThat( description, is( equalTo( "a Matched with " +
                "getFirst()=equalToIgnoringCase(\"Abc\"), " +
                "getSecond()=trimmed()=equalToIgnoringCase(\"Def\")" ) ) );
    }

    @Test
    public void describeTextEquivalenceMismatchWithCaseAndTrimOption( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecificationWithCaseAndTrim.class, Matched.class )
                    .equivalentTo( new Matched( "Abc", "\tDef " ) );
        final Matched actual = new Matched( "ABC", "de f" );
        // Exercise
        final String description = describeMismatch( matcher, actual );
        // Verify
        assertThat( matcher.matches( actual ), is( false ) );
        assertThat( description, is( equalTo( "getSecond() trimmed() was de f" ) ) );
    }

    public static class Matched {
        public Matched( final String first, final String second ) {
            this.first = first;
            this.second = second;
        }

        public String getFirst( ) {
            return first;
        }

        public String getSecond( ) {
            return second;
        }

        private final String first;
        private final String second;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface MatcherSpecificationWithCaseOptionOnly {
        @Text
        String getFirst( );

        @Text( options = IGNORE_CASE )
        String getSecond( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface MatcherSpecificationWithCaseAndTrim {
        @Text( options = IGNORE_CASE )
        String getFirst( );

        @Text( options = { IGNORE_CASE, IGNORE_LEADING_WHITESPACE, IGNORE_TRAILING_WHITESPACE } )
        String getSecond( );
    }

}
