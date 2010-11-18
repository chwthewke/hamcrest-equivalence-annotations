package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class AnnotationMatcher<T> extends TypeSafeMatcher<T> {

    // TODO need to pass in the Class<T> object ?
    public AnnotationMatcher( final T expected,
            final Class<?> matcherSpecification ) {
        this.expected = expected;
        this.matcherSpecification = matcherSpecification;

        initSubMatchers( );
    }

    public void describeTo( final Description description ) {

        final MatcherOf annotation =
                matcherSpecification.getAnnotation( MatcherOf.class );
        description
            .appendText( "a " )
            .appendText( annotation.value( ).getSimpleName( ) );

        // TODO Auto-generated method stub

    }

    @Override
    protected boolean matchesSafely( final T item ) {
        // TODO Auto-generated method stub
        return false;
    }

    private void initSubMatchers( ) {
        final Method[ ] methods = matcherSpecification.getMethods( );
        for ( final Method propertyMethod : methods )
        {
            final SubMatcher<T> sub;
        }
    }

    private final T expected;
    private final Class<?> matcherSpecification;

    private final Collection<SubMatcher<T>> subMatchers =
            new ArrayList<SubMatcher<T>>( );

}
