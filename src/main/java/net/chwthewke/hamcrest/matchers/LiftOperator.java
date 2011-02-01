package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.LiftedEquivalence;

import com.google.common.base.Function;

final class LiftOperator<T, U> {

    public static <T, U> LiftOperator<T, U> liftWith( final Function<T, U> propertyFunction ) {
        return new LiftOperator<T, U>( propertyFunction );
    }

    private LiftOperator( final Function<T, U> propertyFunction ) {
        this.propertyFunction = propertyFunction;
    }

    public Equivalence<T> liftEquivalence( final String name, final Equivalence<? super U> equivalence ) {
        return LiftedEquivalence.create( name, propertyFunction, equivalence );
    }

    private final Function<T, U> propertyFunction;
}
