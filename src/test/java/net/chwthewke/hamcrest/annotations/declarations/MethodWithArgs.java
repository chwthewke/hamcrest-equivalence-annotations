package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.Equals;
import net.chwthewke.hamcrest.annotations.MatcherOf;

public class MethodWithArgs {

    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        @Equals
        String getName( );
    }

    public static class Matched {
        public Matched( final String name ) {
            this.name = name;
        }

        public String getName( final Object arg ) {
            return name + arg.toString( );
        }

        private final String name;
    }
}
