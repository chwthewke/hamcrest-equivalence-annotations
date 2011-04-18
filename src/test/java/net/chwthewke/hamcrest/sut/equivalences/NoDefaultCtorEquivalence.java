package net.chwthewke.hamcrest.sut.equivalences;

import static org.hamcrest.Matchers.greaterThan;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

public class NoDefaultCtorEquivalence implements Equivalence<Integer> {

    public NoDefaultCtorEquivalence( @SuppressWarnings( "unused" ) final Object arg ) {
    }

    public Matcher<Integer> equivalentTo( final Integer expected ) {
        return greaterThan( expected );
    }

}
