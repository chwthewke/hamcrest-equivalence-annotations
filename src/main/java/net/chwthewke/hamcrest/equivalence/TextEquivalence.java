package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matcher;

/**
 * The {@link EqualityEquivalence} class defines equivalence as equality on {@link String}s
 * starting with strict equality that can be relaxed by a number of options defined by the nested
 * enum {@link Option}.
 */
public class TextEquivalence implements Equivalence<String> {

    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An object of the acted upon type.
     * @return A {@link Matcher} that matches objects equivalent to <code>expected</code>.
     */
    public Matcher<String> equivalentTo( final String expected ) {
        return equalTo( expected );
    }

    /**
     * The options for TextEquivalence. These options can be combined, however some distinct
     * combinations may be equivalent.
     */
    public enum Option {
        IGNORE_CASE,
        IGNORE_LEADING_WHITESPACE,
        IGNORE_TRAILING_WHITESPACE,
        IGNORE_WHITESPACE
    }
}
