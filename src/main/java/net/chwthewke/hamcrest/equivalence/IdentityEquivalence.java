package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.sameInstance;

import org.hamcrest.Matcher;

/**
 * The {@link IdentityEquivalence} class defines equivalence as equality by <code>==</code>.
 * 
 * @param <U>
 *            The type this equivalence acts upon.
 */
public final class IdentityEquivalence<U> implements Equivalence<U> {
    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An object of the acted upon type.
     * @return A {@link Matcher} that matches objects equivalent to <code>expected</code>.
     */
    public Matcher<U> equivalentTo( final U expected ) {
        return sameInstance( expected );
    }
}
