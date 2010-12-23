package net.chwthewke.hamcrest.annotations;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

class SubMatcherTemplate<T, U> {

    static <T, U> SubMatcherTemplate<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        return new SubMatcherTemplate<T, U>( propertyName, propertyMethod, matcherFactory );
    }

    private SubMatcherTemplate( final String propertyName,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        this.propertyName = propertyName;
        this.propertyMethod = propertyMethod;
        this.matcherFactory = matcherFactory;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    public U extractProperty( final T item ) {
        return propertyMethod.apply( item );
    }

    public Matcher<? super U> specializeFor( final T expected ) {
        return matcherFactory.apply( propertyMethod.apply( expected ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Function<U, Matcher<? super U>> matcherFactory;

}
