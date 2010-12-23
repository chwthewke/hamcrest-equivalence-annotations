package net.chwthewke.hamcrest.annotations.att2;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.hamcrest.Matcher;

public class CompositeMatcherFactory<T> {

    CompositeMatcherFactory( final Class<T> matchedClass, final Class<?> matcherSpecification ) {
        this.matchedClass = checkNotNull( matchedClass );
        this.matcherSpecification = checkNotNull( matcherSpecification );
    }

    public Matcher<? super T> matcherOf( final T expected ) {
        return new CompositeMatcher<T>( matchedClass, subMatcherProviders, expected );
    }

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final Collection<SubMatcherProvider<T, ?>> subMatcherProviders = newArrayList( );
}
