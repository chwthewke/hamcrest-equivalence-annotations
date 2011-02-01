package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.Equivalence;


/**
 * This class has some factory methods to create {@link Equivalence} instances from <em>equivalence specifications</em> (i.e. classes
 * annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn}).
 */
public final class EquivalenceMatchers {

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with
     *            <code>value()</code> equal to <code>matchedType</code>).
     * @param matchedType
     *            The matched type.
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     */
    public static <T> Equivalence<T> asSpecifiedBy( final Class<?> equivalenceSpecification,
            final Class<T> matchedType ) {
        return CompositeEquivalence.asSpecifiedBy( equivalenceSpecification, matchedType );
    }

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with
     *            <code>value()</code> equal to <code>matchedType</code>).
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     *         <p>
     *         Note: this convenience method allows to eschew the second parameter on the condition that the equivalence specification
     *         extend the marker interface {@link EquivalenceSpecification}. Moreover, this is less safe than the alternative, as the
     *         matched type cannot be checked at runtime.
     */
    public static <T> Equivalence<T>
            asSpecifiedBy( final Class<? extends EquivalenceSpecification<T>> equivalenceSpecification ) {
        return CompositeEquivalence.asSpecifiedBy( equivalenceSpecification );
    }

    private EquivalenceMatchers( ) {
    }
}
