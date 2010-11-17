package net.chwthewke.hamcrest.annotations;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class AnnotationMatcher<T> extends TypeSafeMatcher<T> {

    // TODO need to pass in the Class<T> object ?
    public AnnotationMatcher( final T expected,
            final Class<?> matcherSpecification ) {
    }

    public void describeTo( final Description description ) {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean matchesSafely( final T item ) {
        // TODO Auto-generated method stub
        return false;
    }

}
