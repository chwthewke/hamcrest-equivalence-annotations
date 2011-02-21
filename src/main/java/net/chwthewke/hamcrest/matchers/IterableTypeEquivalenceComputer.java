package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class IterableTypeEquivalenceComputer extends CoreTypeEquivalenceComputer {

    IterableTypeEquivalenceComputer( final EquivalenceFactory equivalenceFactory,
            final OnIterableElements elementTypeAnnotation,
            final boolean enforceOrder ) {
        super( equivalenceFactory );
        this.equivalenceFactory = equivalenceFactory;
        this.enforceOrder = enforceOrder;

        elementType = elementTypeAnnotation.elementType( );
    }

    @SuppressWarnings( "unchecked" )
    public TypeEquivalence<?> computeEquivalenceOnPropertyType( final Annotation equivalenceAnnotation,
            final Class<?> propertyType ) {

        checkState( Iterable.class.isAssignableFrom( propertyType ),
            "'propertyType' must be a subtype of Iterable." );

        final Equivalence<?> equivalenceOnElementType =
                computeCoreAnnotationEquivalence( equivalenceAnnotation, elementType )
                    .getEquivalence( );

        return liftToIterable( equivalenceOnElementType, (Class<? extends Iterable<?>>) propertyType );
    }

    private <X extends Iterable<?>> TypeEquivalence<X> liftToIterable( final Equivalence<?> equivalenceOnElementType,
            final Class<X> propertyType ) {

        @SuppressWarnings( "unchecked" )
        final Equivalence<X> equivalenceOnProperty =
                (Equivalence<X>) equivalenceFactory.createIterableEquivalence( equivalenceOnElementType, enforceOrder );

        return new TypeEquivalence<X>( equivalenceOnProperty, propertyType );
    }

    private final EquivalenceFactory equivalenceFactory;
    private final Class<?> elementType;
    private final boolean enforceOrder;
}
