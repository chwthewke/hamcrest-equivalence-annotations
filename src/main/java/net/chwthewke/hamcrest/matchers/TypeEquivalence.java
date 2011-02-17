package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;

class TypeEquivalence<U> {

    public TypeEquivalence( final Equivalence<U> equivalence, final Class<U> type ) {
        this.equivalence = equivalence;
        this.type = type;
    }

    public Equivalence<U> getEquivalence( ) {
        return equivalence;
    }

    public Class<U> getType( ) {
        return type;
    }

    private final Equivalence<U> equivalence;
    private final Class<U> type;
}
