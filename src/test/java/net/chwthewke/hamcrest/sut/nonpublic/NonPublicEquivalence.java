package net.chwthewke.hamcrest.sut.nonpublic;

import static org.hamcrest.Matchers.greaterThan;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

class NonPublicEquivalence implements Equivalence<Integer> {

    public Matcher<Integer> equivalentTo( final Integer expected ) {
        return greaterThan( expected );
    }

}
