package net.chwthewke.hamcrest.equivalence;

/**
 * {@link IterableEquivalences} allows to create equivalences that act upon Iterables of a type given an equivalence on
 * the component type. The resulting equivalence may optionally ignore the order of elements in the sequences it
 * receives.
 * 
 * @deprecated Use the IterableEquivalence ctor.
 */
@Deprecated
public final class IterableEquivalences {

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
        return new IterableEquivalence<T>( equivalence, false );
    }

    private IterableEquivalences( ) {
    }
}
