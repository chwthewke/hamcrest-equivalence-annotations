package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
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
        assertThat( description, is( equalTo( "" ) ) );
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

}
