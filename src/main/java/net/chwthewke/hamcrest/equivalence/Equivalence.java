package net.chwthewke.hamcrest.equivalence;

import org.hamcrest.Matcher;

/**
 * This interface defines a relation over a type, computed and described with hamcrest matchers.
 * 
 * @param <T>
 *            The type over which the relation acts.
 */
public interface Equivalence<T> {
    /**
     * Partially evaluates the relation.
     * 
     * @param expected
     *            An object of the acted-upon type.
     * @return A {@link Matcher} (predicate) of equivalence to the expected object.
     */
    Matcher<T> equivalentTo( final T expected );
}
