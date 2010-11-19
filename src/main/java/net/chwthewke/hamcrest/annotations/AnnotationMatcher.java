package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class AnnotationMatcher<T> extends TypeSafeMatcher<T> {

    // TODO factory method that takes the Class<T> object from the annotation
    public AnnotationMatcher( final T expected,
            final Class<T> matchedClass,
            final Class<?> matcherSpecification ) {
        this.expected = expected;
        this.matcherSpecification = matcherSpecification;
        this.matchedClass = matchedClass;

        checkMatchedClass( );
        try
        {
            initSubMatchers( );
        }
        catch ( final IllegalAccessException e )
        {
            throw new RuntimeException( e ); // TODO exception
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException( e ); // TODO exception
        }
    }

    public void describeTo( final Description description ) {

        description
            .appendText( "a " )
            .appendText( matchedClass.getSimpleName( ) );

        for ( final SubMatcher<T> subMatcher : subMatchers )
        {
            description
                .appendText( " " )
                .appendDescriptionOf( subMatcher );
        }
    }

    private void checkMatchedClass( ) {
        final MatcherOf annotation =
                matcherSpecification.getAnnotation( MatcherOf.class );
        if ( annotation == null )
            throw new IllegalArgumentException( /* TODO */);
        if ( annotation.value( ) != matchedClass ) // TODO can use isAssignableFrom ?
            throw new IllegalArgumentException( /* TODO */);
    }

    @Override
    protected boolean matchesSafely( final T item ) {
        // TODO Auto-generated method stub
        return false;
    }

    private void initSubMatchers( ) throws IllegalAccessException, InvocationTargetException {
        final Method[ ] methods = matcherSpecification.getMethods( );
        for ( final Method propertyMethod : methods )
        {
            // TODO find matching method on real class dumbass!

            // TODO expected property binding time
            final Object expectedProperty = propertyMethod.invoke( expected );

            final SubMatcher<T> sub = new SubMatcher<T>( propertyMethod, equalTo( expectedProperty ) );
            subMatchers.add( sub );
        }
    }

    private final T expected;
    private final Class<?> matcherSpecification;
    private final Class<T> matchedClass;

    private final Collection<SubMatcher<T>> subMatchers =
            new ArrayList<SubMatcher<T>>( );

}
