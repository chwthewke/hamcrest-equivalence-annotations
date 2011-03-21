package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

class WidenedEquivalence<T, U extends T> implements Equivalence<T> {

    WidenedEquivalence( final Equivalence<U> delegate, final Class<U> originalTargetType ) {
        this.delegate = delegate;
        this.originalTargetType = originalTargetType;
    }

    @SuppressWarnings( "unchecked" )
    public Matcher<T> equivalentTo( final T expected ) {

        checkArgument(
            originalTargetType.isInstance( expected ),
            String.format(
                "This equivalence can only produce matchers for members of type %s.",
                originalTargetType.getName( ) ) );

        return new WidenedMatcher( delegate.equivalentTo( (U) expected ) );
    }

    private final Equivalence<U> delegate;
    private final Class<U> originalTargetType;

    private class WidenedMatcher extends TypeSafeDiagnosingMatcher<T> {

        public WidenedMatcher( final Matcher<U> delegateMatcher ) {
            this.delegateMatcher = delegateMatcher;
        }

        public void describeTo( final Description description ) {
            delegateMatcher.describeTo( description );
        }

        @Override
        protected boolean matchesSafely( final T item, final Description mismatchDescription ) {

            if ( !originalTargetType.isInstance( item ) )
            {
                mismatchDescription
                    .appendText( "was an instance of the incompatible type " )
                    .appendValue( item.getClass( ).getName( ) );
                return false;
            }

            final boolean matches = delegateMatcher.matches( item );
            if ( !matches )
                delegateMatcher.describeMismatch( item, mismatchDescription );
            return matches;
        }

        private final Matcher<U> delegateMatcher;
    }

}
