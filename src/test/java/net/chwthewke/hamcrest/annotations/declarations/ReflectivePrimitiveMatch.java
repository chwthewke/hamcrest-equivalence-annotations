package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.MatcherOf;

public class ReflectivePrimitiveMatch {

    public static class Matched {

        public Matched( final int first, final int second ) {
            this.first = first;
            this.second = second;
        }

        public int getFirst( ) {
            return first;
        }

        public int getSecond( ) {
            return second;
        }

        private final int first;
        private final int second;
    }

    @MatcherOf( Matched.class )
    public static interface MatcherSpecification {
        @Equality
        int getFirst( );

        @Identity
        int getSecond( );
    }

}
