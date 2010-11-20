package net.chwthewke.hamcrest.annotations;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;

public class SubMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    SubMatcher( final String propertyName,
            final Function<T, ?> propertyExtractor,
            final Matcher<?> propertyMatcher ) {
        this.matcher = propertyMatcher;
        this.propertyName = propertyName;
        this.propertyExtractor = propertyExtractor;
    }

    @Override
    public boolean matchesSafely( final T item, final Description mismatchDescription ) {
        final Object matchedProperty = extract( item );

        final boolean matches = matcher.matches( matchedProperty );
        if ( !matches )
        {
            mismatchDescription
                .appendText( propertyName )
                .appendText( "() " );
            matcher.describeMismatch( matchedProperty, mismatchDescription );
        }
        return matches;
    }

    public void describeTo( final Description description ) {
        description
            .appendText( propertyName )
            .appendText( "()=" )
            .appendDescriptionOf( matcher );
    }

    private Object extract( final T item ) {
        return propertyExtractor.apply( item );
    }

    private final Matcher<?> matcher;

    private final String propertyName;
    private final Function<T, ?> propertyExtractor;
}
