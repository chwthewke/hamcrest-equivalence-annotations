package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;

@Deprecated
class TypeEquivalence<U> {

    public TypeEquivalence( final Equivalence<U> equivalence, final Class<U> type ) {
        this.equivalence = equivalence;
    }

    public Equivalence<U> getEquivalence( ) {
        return equivalence;
    }

    private final Equivalence<U> equivalence;
}
