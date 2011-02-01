package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

public final class EqualityEquivalence<U> implements Equivalence<U> {
    public Matcher<U> equivalentTo( final U expected ) {
        return equalTo( expected );
    }
}
