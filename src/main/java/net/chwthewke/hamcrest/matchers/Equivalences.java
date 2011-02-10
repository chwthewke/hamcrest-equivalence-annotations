package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.equivalence.Equivalence;

/**
 * This class has some factory methods to create {@link Equivalence} instances from <em>equivalence specifications</em> (i.e. classes
 * annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn}).
 */
public final class Equivalences {

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with
     *            {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with <code>value()</code> equal to
     *            <code>matchedType</code>).
     * @param matchedType
     *            The matched type.
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     */
    public static <T> Equivalence<T> asSpecifiedBy( final Class<?> equivalenceSpecification,
            final Class<T> matchedType ) {
        return new CompositeEquivalence<T>(
            PROPERTY_FINDER,
            SPECIFICATION_VALIDATOR,
            matchedType,
            equivalenceSpecification );
    }

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with
     *            {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with <code>value()</code> equal to
     *            <code>matchedType</code>).
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     *         <p>
     *         Note: this convenience method allows to eschew the second parameter on the condition that the equivalence specification
     *         extend the marker interface {@link EquivalenceSpecification}. Moreover, this is less safe than the alternative, as the
     *         matched type cannot be checked at runtime.
     */
    public static <T> Equivalence<T>
            asSpecifiedBy( final Class<? extends EquivalenceSpecification<T>> equivalenceSpecification ) {
        SPECIFICATION_VALIDATOR.validateSpecificationInterface( equivalenceSpecification );

        @SuppressWarnings( "unchecked" )
        final Class<T> targetType =
                (Class<T>) equivalenceSpecification.getAnnotation( EquivalenceSpecificationOn.class ).value( );
        return new CompositeEquivalence<T>(
            PROPERTY_FINDER,
            SPECIFICATION_VALIDATOR,
            targetType,
            equivalenceSpecification );
    }

    private Equivalences( ) {
    }

    private static final EquivalenceSpecificationValidator SPECIFICATION_VALIDATOR = new EquivalenceSpecificationValidator( );
    private static final PropertyFinder PROPERTY_FINDER = new PropertyFinder( );
}
