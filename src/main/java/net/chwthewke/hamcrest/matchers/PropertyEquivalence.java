package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

final class PropertyEquivalence<T, U> {

    static <T, U> PropertyEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Equivalence<? super U> equivalence ) {
        return new PropertyEquivalence<T, U>( propertyName, propertyMethod, equivalence );
    }

    private PropertyEquivalence( final String propertyName,
                                 final Function<T, U> propertyMethod,
                                 final Equivalence<? super U> equivalence ) {
        this.propertyName = propertyName;
        this.propertyMethod = propertyMethod;
        this.equivalence = equivalence;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    public U extractPropertyValue( final T item ) {
        return propertyMethod.apply( item );
    }

    public Matcher<? super U> specializeFor( final T expected ) {
        return equivalence.equivalentTo( propertyMethod.apply( expected ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Equivalence<? super U> equivalence;

}
