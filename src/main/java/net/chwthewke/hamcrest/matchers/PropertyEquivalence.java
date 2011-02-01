package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

final class PropertyEquivalence<T, U> {

    static <T, U> PropertyEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Equivalence<? super U> equivalence ) {
        return new PropertyEquivalence<T, U>( propertyName, propertyMethod,
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U input ) {
                        return equivalence.equivalentTo( input );
                    }
                } );
    }

    /**
     * @deprecated Use other factory method
     * @param <T>
     * @param <U>
     * @param propertyName
     * @param propertyMethod
     * @param matcherFactory
     * @return
     */
    @Deprecated
    static <T, U> PropertyEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        return new PropertyEquivalence<T, U>( propertyName, propertyMethod, matcherFactory );
    }

    private PropertyEquivalence( final String propertyName,
                                 final Function<T, U> propertyMethod,
                                 final Function<U, Matcher<? super U>> matcherFactory ) {
        this.propertyName = propertyName;
        this.propertyMethod = propertyMethod;
        this.matcherFactory = matcherFactory;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    public U extractPropertyValue( final T item ) {
        return propertyMethod.apply( item );
    }

    public Matcher<? super U> specializeFor( final T expected ) {
        return matcherFactory.apply( propertyMethod.apply( expected ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Function<U, Matcher<? super U>> matcherFactory;

}
