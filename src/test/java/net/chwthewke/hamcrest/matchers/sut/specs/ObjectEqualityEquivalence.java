package net.chwthewke.hamcrest.matchers.sut.specs;

import static org.hamcrest.Matchers.equalTo;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

public class ObjectEqualityEquivalence implements Equivalence<Object> {

    public Matcher<Object> equivalentTo( final Object expected ) {
        return equalTo( expected );
    }

}
