package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newEnumSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.Set;

import org.hamcrest.Matcher;

/**
 * The {@link EqualityEquivalence} class defines equivalence as equality on {@link String}s
 * starting with strict equality that can be relaxed by a number of options defined by the nested
 * enum {@link TextEquivalenceOption}.
 */
class TextEquivalenceCore implements Equivalence<String> {
    TextEquivalenceCore( final TextEquivalenceOption... options ) {
        this.options = newEnumSet( newArrayList( options ), TextEquivalenceOption.class );
    }

    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An object of the acted upon type.
     * @return A {@link Matcher} that matches objects equivalent to <code>expected</code>.
     */
    public Matcher<String> equivalentTo( final String expected ) {
        return options.contains( TextEquivalenceOption.IGNORE_CASE ) ?
                equalToIgnoringCase( expected ) :
                equalTo( expected );
    }

    private final Set<TextEquivalenceOption> options;
}
