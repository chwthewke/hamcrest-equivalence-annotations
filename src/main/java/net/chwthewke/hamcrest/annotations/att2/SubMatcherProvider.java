package net.chwthewke.hamcrest.annotations.att2;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

class SubMatcherProvider<T, U> {

    static <T, U> SubMatcherProvider<T, U> create(
            final String propertyName,
            final Class<U> propertyType,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        return new SubMatcherProvider<T, U>( propertyName, propertyType,
                propertyMethod, matcherFactory );
    }

    private SubMatcherProvider( final String propertyName,
            final Class<U> propertyType,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyMethod = propertyMethod;
        this.matcherFactory = matcherFactory;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    public U extractProperty( final T item ) {
        return propertyMethod.apply( item );
    }

    public Matcher<? super U> matcherOf( final T expected ) {
        return matcherFactory.apply( propertyMethod.apply( expected ) );
    }

    private final String propertyName;
    private final Class<U> propertyType;
    private final Function<T, U> propertyMethod;
    private final Function<U, Matcher<? super U>> matcherFactory;

}
