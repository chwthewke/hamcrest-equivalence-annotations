package net.chwthewke.hamcrest.sut.equivalences;

import static org.hamcrest.Matchers.startsWith;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

public class SimpleStringEquivalence implements Equivalence<String> {

    public Matcher<String> equivalentTo( final String expected ) {
        return startsWith( expected );
    }

}
