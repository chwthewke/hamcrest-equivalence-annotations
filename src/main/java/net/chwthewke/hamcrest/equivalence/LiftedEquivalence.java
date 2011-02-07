package net.chwthewke.hamcrest.equivalence;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

/**
 * {@link LiftedEquivalence} allows to extend an equivalence acting upon a type <code>U</code> to anoter type <code>T</code> using a
 * function of <code>T</code> into <code>U</code>.
 * 
 * @param <T>
 *            The type to lift the equivalence unto.
 * @param <U>
 *            The type to lift the equivalence from.
 */
public class LiftedEquivalence<T, U> implements Equivalence<T> {

    /**
     * Creates an equivalence that considers <code>t1</code> and <code>t2</code> to be equivalent iff <code>projection.apply( t1 )</code>
     * and <code>projection.apply( t2 )</code> are equivalent according to the argument <code>equivalence</code>.
     * 
     * @param name
     *            A name given to this equivalence.
     * @param equivalence
     *            The equivalence to lift.
     * @param projection
     *            The projection along which to lift the equivalence.
     */
    public LiftedEquivalence( final String name,
                                 final Equivalence<? super U> equivalence,
                                 final Function<T, U> projection ) {
        this.name = checkNotNull( name );
        this.projection = checkNotNull( projection );
        this.equivalence = equivalence;
    }

    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An object of the acted upon type.
     * @return A {@link Matcher} that matches objects equivalent to <code>expected</code>.
     */
    public Matcher<T> equivalentTo( final T expected ) {
        return new LiftedMatcher<T, U>( name, projection,
                equivalence.equivalentTo( projection.apply( expected ) ) );
    }

    private final String name;
    private final Function<T, U> projection;
    private final Equivalence<? super U> equivalence;

}
