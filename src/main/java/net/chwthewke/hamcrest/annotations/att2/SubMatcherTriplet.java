package net.chwthewke.hamcrest.annotations.att2;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public class SubMatcherTriplet<T, U> {

    public static <T, U> SubMatcherTriplet<T, U> createSubMatcher(
            final Class<U> propertyType,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        return new SubMatcherTriplet<T, U>( propertyType, propertyMethod, matcherFactory );
    }

    private SubMatcherTriplet( final Class<U> propertyType,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        super( );
        this.propertyType = propertyType;
        this.propertyMethod = propertyMethod;
        this.matcherFactory = matcherFactory;
    }

    private final Class<U> propertyType;
    private final Function<T, U> propertyMethod;
    private final Function<U, Matcher<? super U>> matcherFactory;

}
