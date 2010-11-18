package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class SubMatcher<T> extends TypeSafeMatcher<T> {

    @Override
    public boolean matchesSafely( final T item ) {
        return matcher.matches( extract( item ) );
    }

    public void describeTo( final Description description ) {
        description
            .appendText( "with " )
            .appendText( extractor.getName( ) )
            .appendText( " " )
            .appendDescriptionOf( matcher );
    }

    @SuppressWarnings( "unchecked" )
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

    private Matcher<?> matcher;
    private Method extractor; // Function<T, U> ?
}
