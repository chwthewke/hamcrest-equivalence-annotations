package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class SubMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    SubMatcher( final Method extractor, final Matcher<?> matcher ) {
        this.matcher = matcher;
        this.extractor = extractor;
    }

    @Override
    public boolean matchesSafely( final T item, final Description mismatchDescription ) {
        final Object matchedProperty = extract( item );

        final boolean matches = matcher.matches( matchedProperty );
        if ( !matches )
        {
            mismatchDescription
                .appendText( extractor.getName( ) )
                .appendText( "() " );
            matcher.describeMismatch( matchedProperty, mismatchDescription );
        }
        return matches;
    }

    public void describeTo( final Description description ) {
        description
            .appendText( "with " )
            .appendText( extractor.getName( ) )
            .appendText( "()=" )
            .appendDescriptionOf( matcher );
    }

    private Object extract( final T item ) {
        try
        {
            // TODO type-check at construction
            return extractor.invoke( item );
        }
        catch ( final IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
    }

    private final Matcher<?> matcher;
    private final Method extractor; // Function<T, U> ?
}
