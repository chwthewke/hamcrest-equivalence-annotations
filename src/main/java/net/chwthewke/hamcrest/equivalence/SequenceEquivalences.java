package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Function;

/**
 * {@link SequenceEquivalences} allows to create equivalences that act upon Iterables (or arrays) of a type given an equivalence on
 * the component type. The resulting equivalence may optionally ignore the order of elements in the sequences it receives.
 */
public final class SequenceEquivalences {

    /**
     * Creates an equivalence such that two {@link Iterable}s are equivalent iff their elements are
     * equivalent one-to-one, in order, according to the specified equivalence on the component type.
     * 
     * @param <T>
     *            The component type.
     * @param equivalence
     *            The equivalence on the component type.
     * @return The lifted, in-order equivalence for {@link Iterable}&lt;T&gt;
     */
    public static <T> Equivalence<Iterable<? extends T>> iterableEquivalence( final Equivalence<T> equivalence ) {
        return new IterableEquivalence<T>( equivalence, true );
    }

    /**
     * Creates an equivalence such that two {@link Iterable}s are equivalent iff their elements are
     * equivalent one-to-one, in any order, according to the specified equivalence on the component type.
     * 
     * @param <T>
     *            The component type.
     * @param equivalence
     *            The equivalence on the component type.
     * @return The lifted, not in-order equivalence for {@link Iterable}&lt;T&gt;
     */
    public static <T> Equivalence<Iterable<? extends T>> iterableEquivalenceInAnyOrder( final Equivalence<T> equivalence ) {
        return new IterableEquivalence<T>( equivalence, true );
    }

    /**
     * Creates an equivalence such that two arrays are equivalent iff their elements are
     * equivalent one-to-one, in order, according to the specified equivalence on the component type.
     * 
     * @param <T>
     *            The component type.
     * @param equivalence
     *            The equivalence on the component type.
     * @return The lifted, in-order equivalence for <code>T[ ]</code>;
     */
    public static <T> Equivalence<T[ ]> arrayEquivalence( final Equivalence<T> equivalence ) {
        return liftToArray( iterableEquivalence( equivalence ) );
    }

    /**
     * Creates an equivalence such that two arrays are equivalent iff their elements are
     * equivalent one-to-one, in any order, according to the specified equivalence on the component type.
     * 
     * @param <T>
     *            The component type.
     * @param equivalence
     *            The equivalence on the component type.
     * @return The lifted, not in-order equivalence for <code>T[ ]</code>;
     */
    public static <T> Equivalence<T[ ]> arrayEquivalenceInAnyOrder( final Equivalence<T> equivalence ) {
        return liftToArray( iterableEquivalenceInAnyOrder( equivalence ) );
    }

    private static <T> Equivalence<T[ ]> liftToArray( final Equivalence<Iterable<? extends T>> iterableEquivalence ) {
        return new LiftedEquivalence<T[ ], Iterable<? extends T>>( "",
                iterableEquivalence, SequenceEquivalences.<T>toList( ) );
    }

    private static <T> Function<T[ ], Iterable<? extends T>> toList( ) {
        return new Function<T[ ], Iterable<? extends T>>( ) {
            public Iterable<T> apply( final T[ ] input ) {
                return newArrayList( input );
            }
        };
    }

    private SequenceEquivalences( ) {
    }
}
