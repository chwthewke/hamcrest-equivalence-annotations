package net.chwthewke.hamcrest.equivalence;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

// Probably this is temporary and will need to be moved to src/main
// Will this disappear if we change the sig of Equivalence to "Matcher<? super T> equivalentTo..." ? Can we ?
public class Transforms {

    public static <U, T extends U> Equivalence<T> narrow( final Equivalence<U> equivalence ) {
        return new NarrowingEquivalence<U, T>( equivalence );
    }

    private static class NarrowingEquivalence<U, T extends U> implements Equivalence<T> {

        private NarrowingEquivalence( final Equivalence<U> equivalence ) {
            this.equivalence = equivalence;
        }

        public Matcher<T> equivalentTo( final T expected ) {
            return new NarrowingMatcher<U, T>( equivalence.equivalentTo( expected ) );
        }

        private final Equivalence<U> equivalence;
    }

    private static class NarrowingMatcher<U, T extends U> extends BaseMatcher<T> {

        private NarrowingMatcher( final Matcher<U> matcher ) {
            this.matcher = matcher;
        }

        public boolean matches( final Object item ) {
            return matcher.matches( item );
        }

        public void describeTo( final Description description ) {
            matcher.describeTo( description );
        }

        @Override
        public void describeMismatch( final Object item, final Description description ) {
            matcher.describeMismatch( item, description );
        }

        @Override
        public String toString( ) {
            return matcher.toString( );
        }

        private final Matcher<U> matcher;
    }
}
