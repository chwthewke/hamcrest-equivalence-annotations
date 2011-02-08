package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Sets.newEnumSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.Set;

import org.hamcrest.Matcher;

class TextEquivalenceCore implements Equivalence<String> {
    TextEquivalenceCore( final Set<TextEquivalenceOption> options ) {
        this.options = newEnumSet( options, TextEquivalenceOption.class );
    }

    public Matcher<String> equivalentTo( final String expected ) {
        return options.contains( TextEquivalenceOption.IGNORE_CASE ) ?
                equalToIgnoringCase( expected ) :
                equalTo( expected );
    }

    private final Set<TextEquivalenceOption> options;
}
