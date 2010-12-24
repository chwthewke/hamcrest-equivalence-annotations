package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.MatcherFactory;
import net.chwthewke.hamcrest.MatcherSpecification;

public final class AnnotationMatchers {

    public static <T> MatcherFactory<T> asSpecifiedBy( final Class<?> matcherSpecification,
            final Class<T> matchedClass ) {
        return CompositeMatcherFactory.<T>asSpecifiedBy( matcherSpecification, matchedClass );
    }

    public static <T> MatcherFactory<T>
            asSpecifiedBy( final Class<? extends MatcherSpecification<T>> matcherSpecification ) {
        return CompositeMatcherFactory.<T>asSpecifiedBy( matcherSpecification );
    }

    private AnnotationMatchers( ) {
    }
}
