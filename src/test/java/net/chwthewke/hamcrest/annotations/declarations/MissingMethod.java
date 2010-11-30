package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.Equals;
import net.chwthewke.hamcrest.annotations.MatcherOf;

public class MissingMethod {

    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        // The interface method does not have the exact same name
        // as the intended method on the matched class.
        @Equals
        String name( );
    }

    public static class Matched {
        public Matched( final String name ) {
            this.name = name;
        }

        public String getName( ) {
            return name;
        }

        private final String name;
    }
}
