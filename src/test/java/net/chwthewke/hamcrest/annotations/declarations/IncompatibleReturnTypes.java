package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.Equals;
import net.chwthewke.hamcrest.annotations.MatcherOf;

public class IncompatibleReturnTypes {
    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        @Equals
        Double getName( );
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
