package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.LiftedEquivalence;

import com.google.common.base.Function;

class LiftedEquivalenceFactory {

    public <T, U> LiftedEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Equivalence<? super U> equivalence ) {
        return LiftedEquivalence.create( propertyName, propertyMethod, equivalence );
    }

}
