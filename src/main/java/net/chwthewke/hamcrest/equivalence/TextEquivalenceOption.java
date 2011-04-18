package net.chwthewke.hamcrest.equivalence;

/**
 * The options for {@link TextEquivalence}. These options can be combined, however some distinct
 * combinations may be equivalent.
 */
public enum TextEquivalenceOption {
    IGNORE_CASE,
    IGNORE_LEADING_WHITESPACE,
    IGNORE_TRAILING_WHITESPACE,
    IGNORE_WHITESPACE
}
