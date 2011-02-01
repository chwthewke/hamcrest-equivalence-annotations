package net.chwthewke.hamcrest.equivalence;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;

class LiftedMatcher<T, U> extends TypeSafeDiagnosingMatcher<T> {

    public LiftedMatcher( final String projectionName, final Function<T, U> projection,
            final Matcher<? super U> matcher ) {
        this.projectionName = projectionName;
        this.projection = projection;
        this.matcher = matcher;
    }

    public void describeTo( final Description description ) {
        description
            .appendText( projectionName )
            .appendText( "()=" )
            .appendDescriptionOf( matcher );
    }

    @Override
    protected boolean matchesSafely( final T item, final Description mismatchDescription ) {
        final U projectionValue = projection.apply( item );
        if ( !matcher.matches( projectionValue ) )
        {
            mismatchDescription
                .appendText( projectionName )
                .appendText( "() " );
            matcher.describeMismatch( projectionValue, mismatchDescription );
            return false;
        }
        return true;
    }

    private final String projectionName;
    private final Function<T, U> projection;
    private final Matcher<? super U> matcher;
}
