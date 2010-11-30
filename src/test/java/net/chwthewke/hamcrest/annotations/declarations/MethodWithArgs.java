package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.Equals;
import net.chwthewke.hamcrest.annotations.MatcherOf;

public class MethodWithArgs {

    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        // The interface method does not have the exact same name
        // as the intended method on the matched class.
        @Equals
        String getName( );
    }

    public static class Matched {
        public Matched( final String name ) {
            this.name = name;
        }

        public String getName( final Object widget ) {
            return name + widget.toString( );
        }

        private final String name;
    }
}
