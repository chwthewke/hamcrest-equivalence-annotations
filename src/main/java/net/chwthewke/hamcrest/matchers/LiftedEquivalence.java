package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

final class LiftedEquivalence<T, U> implements Equivalence<T> {

    static <T, U> LiftedEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Equivalence<? super U> equivalence ) {
        return new LiftedEquivalence<T, U>( propertyName, propertyMethod, equivalence );
    }

    private LiftedEquivalence( final String propertyName,
                                 final Function<T, U> propertyMethod,
                                 final Equivalence<? super U> equivalence ) {
        this.propertyName = propertyName;
        this.propertyMethod = propertyMethod;
        this.equivalence = equivalence;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    @Deprecated
    public U extractPropertyValue( final T item ) {
        return propertyMethod.apply( item );
    }

    @Deprecated
    public Matcher<? super U> specializeFor( final T expected ) {
        return equivalence.equivalentTo( propertyMethod.apply( expected ) );
    }

    public Matcher<T> equivalentTo( final T expected ) {
        return new LiftedMatcher<T, U>( propertyName, propertyMethod,
                equivalence.equivalentTo( propertyMethod.apply( expected ) ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Equivalence<? super U> equivalence;

}
