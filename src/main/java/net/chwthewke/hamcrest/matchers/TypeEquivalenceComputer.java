package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.ArrayEquivalences;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class TypeEquivalenceComputer {

    TypeEquivalenceComputer(
            final EquivalenceFactory equivalenceFactory,
            final BasicTypeEquivalenceComputer basicTypeEquivalenceComputer ) {
        this.equivalenceFactory = equivalenceFactory;
        this.basicTypeEquivalenceComputer = basicTypeEquivalenceComputer;
    }

    // TODO refactor to decouple from "Method" (pass type and 'set' of annotations)
    public <T> TypeEquivalence<?> computeEquivalenceOnPropertyType( final TypeEquivalenceSpecification<T> specification ) {
        final Annotation equivalenceAnnotation =
                specification.getEquivalenceAnnotation( );

        final Class<?> propertyType = specification.getTargetType( );

        if ( specification.hasContainerAnnotation( )
                && specification.getContainerAnnotation( ).annotationType( ) == OnIterableElements.class )
        {
            checkArgument( Iterable.class.isAssignableFrom( propertyType ),
                "'propertyType' must be a subtype of Iterable." );

            final OnIterableElements onElementsAnnotation = (OnIterableElements) specification.getContainerAnnotation( );
            final Class<?> elementType = onElementsAnnotation.elementType( );

            final Equivalence<?> equivalenceOnElementType = basicTypeEquivalenceComputer
                .computeEquivalenceOnBasicType( equivalenceAnnotation, elementType )
                .getEquivalence( );

            @SuppressWarnings( "unchecked" )
            final Class<? extends Iterable<?>> propertyTypeAsIterable = (Class<? extends Iterable<?>>) propertyType;

            return liftToIterable( equivalenceOnElementType, propertyTypeAsIterable, onElementsAnnotation.inOrder( ) );
        }

        if ( specification.hasContainerAnnotation( )
                && specification.getContainerAnnotation( ).annotationType( ) == OnArrayElements.class )
        {
            checkArgument( propertyType.isArray( ), "'propertyType' must be an array type." );

            final OnArrayElements onElementsAnnotation = (OnArrayElements) specification.getContainerAnnotation( );
            final Class<?> elementType = propertyType.getComponentType( );

            final Equivalence<?> equivalenceOnElementType = basicTypeEquivalenceComputer
                .computeEquivalenceOnBasicType( equivalenceAnnotation, elementType )
                .getEquivalence( );
            final TypeEquivalence<Iterable<?>> iterableEquivalence = liftToIterable( equivalenceOnElementType,
                (Class) Iterable.class,
                onElementsAnnotation.inOrder( ) );
            return new TypeEquivalence(
                ArrayEquivalences.<Object>forArrays( iterableEquivalence.getEquivalence( ) ), propertyType );
        }

        return basicTypeEquivalenceComputer.computeEquivalenceOnBasicType( equivalenceAnnotation, propertyType );
    }

    private <X extends Iterable<?>> TypeEquivalence<X> liftToIterable( final Equivalence<?> equivalenceOnElementType,
            final Class<X> propertyType, final boolean enforceOrder ) {

        @SuppressWarnings( "unchecked" )
        final Equivalence<X> equivalenceOnProperty =
                (Equivalence<X>) equivalenceFactory.createIterableEquivalence( equivalenceOnElementType, enforceOrder );

        return new TypeEquivalence<X>( equivalenceOnProperty, propertyType );
    }

    private final EquivalenceFactory equivalenceFactory;
    private final BasicTypeEquivalenceComputer basicTypeEquivalenceComputer;
}
