package net.chwthewke.hamcrest.annotations;

import org.junit.Ignore;

@Ignore
public class TextMatchersTest {

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
    public static interface MatcherSpecification {
        @Text( options = { } )
        String getFirst( );

        @Identity
        String getSecond( );
    }

}
