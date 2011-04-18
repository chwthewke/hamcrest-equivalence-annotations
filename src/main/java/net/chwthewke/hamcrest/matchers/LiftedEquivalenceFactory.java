package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.LiftedEquivalence;

import com.google.common.base.Function;

class LiftedEquivalenceFactory {

    public <T, U> LiftedEquivalence<T, U> create(
            final String propertyName,
            final Equivalence<? super U> equivalence,
            final Function<T, U> propertyMethod ) {
        return new LiftedEquivalence<T, U>( propertyName, equivalence, propertyMethod );
    }

}
