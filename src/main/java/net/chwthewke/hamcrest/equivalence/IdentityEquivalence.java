package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.Matcher;

public final class IdentityEquivalence<U> implements Equivalence<U> {
    public Matcher<U> equivalentTo( final U expected ) {
        return sameInstance( expected );
    }
}
