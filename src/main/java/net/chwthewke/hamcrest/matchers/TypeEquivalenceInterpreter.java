package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class TypeEquivalenceInterpreter {

    TypeEquivalenceInterpreter(
            final EquivalenceFactory equivalenceFactory,
            final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter ) {
        this.equivalenceFactory = equivalenceFactory;
        this.basicTypeEquivalenceInterpreter = basicTypeEquivalenceInterpreter;
    }

    // TODO rename, as well as similar method on BTEI
    public <T> TypeEquivalence<?> computeEquivalenceOnPropertyType( final TypeEquivalenceSpecification<T> specification ) {

        final Annotation equivalenceAnnotation = specification.getEquivalenceAnnotation( );

        final Class<T> propertyType = specification.getTargetType( );

        if ( !specification.hasContainerAnnotation( ) )
            return interpretEquivalenceOnBasicType( equivalenceAnnotation, propertyType );

        final Annotation containerAnnotation = specification.getContainerAnnotation( );
        if ( containerAnnotation.annotationType( ) == OnIterableElements.class )
        {
            final OnIterableElements onIterableAnnotation = (OnIterableElements) containerAnnotation;

            final boolean inOrder = onIterableAnnotation.inOrder( );
            final Class<?> elementType = onIterableAnnotation.elementType( );

            return createIterableEquivalence( equivalenceAnnotation,
                inOrder, propertyType, elementType );
        }

        throw new IllegalArgumentException(
            String.format( "Unknown container annotation %s", containerAnnotation.annotationType( ).getSimpleName( ) ) );

//        if ( specification.hasContainerAnnotation( )
//                && containerAnnotation.annotationType( ) == OnArrayElements.class )
//        {
//            checkArgument( propertyType.isArray( ), "'propertyType' must be an array type." );
//
//            final OnArrayElements onElementsAnnotation = (OnArrayElements) containerAnnotation;
//            final Class<?> elementType = propertyType.getComponentType( );
//
//            final Equivalence<?> equivalenceOnElementType = basicTypeEquivalenceInterpreter
//                .createTypeEquivalence( equivalenceAnnotation, elementType )
//                .getEquivalence( );
//            final TypeEquivalence<Iterable<?>> iterableEquivalence = liftToIterable( equivalenceOnElementType,
//                (Class) Iterable.class,
//                onElementsAnnotation.inOrder( ) );
//            return new TypeEquivalence(
//                ArrayEquivalences.<Object>forArrays( iterableEquivalence.getEquivalence( ) ), propertyType );
//        }
//
//        return basicTypeEquivalenceInterpreter.createTypeEquivalence( equivalenceAnnotation, propertyType );
    }

    private <T, U> TypeEquivalence<?> createIterableEquivalence( final Annotation equivalenceAnnotation,
            final boolean inOrder, final Class<T> iterableType, final Class<U> elementType ) {
        checkArgument( Iterable.class.isAssignableFrom( iterableType ),
            "'propertyType' must be a subtype of Iterable." );

        final TypeEquivalence<? super U> equivalenceOnElements =
                interpretEquivalenceOnBasicType( equivalenceAnnotation, elementType );

        final Equivalence<Iterable<? extends U>> iterableEquivalence =
                equivalenceFactory.<U>createIterableEquivalence(
                    equivalenceOnElements.getEquivalence( ),
                    inOrder );

        @SuppressWarnings( { "unchecked", "rawtypes" } )
        final Class<Iterable<? extends U>> coercedIterableType = (Class) iterableType;

        return new TypeEquivalence<Iterable<? extends U>>( iterableEquivalence, coercedIterableType );
    }

    private <T> TypeEquivalence<? super T> interpretEquivalenceOnBasicType( final Annotation equivalenceAnnotation,
            final Class<T> propertyType ) {
        return basicTypeEquivalenceInterpreter.createTypeEquivalence( equivalenceAnnotation, propertyType );
    }

//    private <X extends Iterable<?>> TypeEquivalence<X> liftToIterable( final Equivalence<?> equivalenceOnElementType,
//            final Class<X> propertyType, final boolean enforceOrder ) {
//
//        @SuppressWarnings( "unchecked" )
//        final Equivalence<X> equivalenceOnProperty =
//                (Equivalence<X>) equivalenceFactory.createIterableEquivalence( equivalenceOnElementType, enforceOrder );
//
//        return new TypeEquivalence<X>( equivalenceOnProperty, propertyType );
//    }

    private final EquivalenceFactory equivalenceFactory;
    private final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter;
}
