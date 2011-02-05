package net.chwthewke.hamcrest.sut.equivalences;

import static org.hamcrest.Matchers.greaterThan;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

public class BadEquivalence implements Equivalence<Integer> {

    public BadEquivalence( ) {
        throw new NullPointerException( );
    }

    public Matcher<Integer> equivalentTo( final Integer expected ) {
        return greaterThan( expected );
    }

}
