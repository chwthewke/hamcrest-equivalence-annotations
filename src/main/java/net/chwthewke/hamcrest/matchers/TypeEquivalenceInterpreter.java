package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.primitives.Primitives.wrap;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.booleanArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.byteArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.charArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.doubleArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.floatArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.intArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.longArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.shortArrayEquivalence;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.ArrayEquivalences;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.base.Function;

class TypeEquivalenceInterpreter {

    TypeEquivalenceInterpreter(
            final EquivalenceFactory equivalenceFactory,
            final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter ) {
        this.equivalenceFactory = equivalenceFactory;
        this.basicTypeEquivalenceInterpreter = basicTypeEquivalenceInterpreter;
    }

    // TODO rename, as well as similar method on BTEI
    public <T> TypeEquivalence<? super T> computeEquivalenceOnPropertyType(
            final TypeEquivalenceSpecification<T> specification ) {

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

        if ( containerAnnotation.annotationType( ) == OnArrayElements.class )
        {
            final OnArrayElements onArrayAnnotation = (OnArrayElements) containerAnnotation;

            final boolean inOrder = onArrayAnnotation.inOrder( );

            checkArgument( propertyType.isArray( ), "'propertyType' must be an array type." );

            final Class<?> elementType = propertyType.getComponentType( );

            return createArrayEquivalence( equivalenceAnnotation,
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

    private <T, U> TypeEquivalence<? super T> createIterableEquivalence( final Annotation equivalenceAnnotation,
            final boolean inOrder, final Class<T> iterableType, final Class<U> elementType ) {
        checkArgument( Iterable.class.isAssignableFrom( iterableType ),
            "'propertyType' must be a subtype of Iterable." );

        final TypeEquivalence<? super U> equivalenceOnElements =
                interpretEquivalenceOnBasicType( equivalenceAnnotation, elementType );

        final Equivalence<Iterable<? extends U>> iterableEquivalence =
                equivalenceFactory.<U>createIterableEquivalence(
                    equivalenceOnElements.getEquivalence( ),
                    inOrder );

        return new TypeEquivalence<T>( (Equivalence<T>) iterableEquivalence, iterableType );
    }

    private <T, U> TypeEquivalence<? super T> createArrayEquivalence( final Annotation equivalenceAnnotation,
            final boolean inOrder, final Class<T> arrayType, final Class<U> elementType ) {

        final TypeEquivalence<? super U> equivalenceOnElements =
                interpretEquivalenceOnBasicType( equivalenceAnnotation,
                    wrap( elementType ) );

        if ( elementType == boolean.class )
        {
            final Equivalence<Iterable<? extends Boolean>> iterableEquivalence =
                    equivalenceFactory.<Boolean>createIterableEquivalence(
                        (Equivalence<Boolean>) equivalenceOnElements.getEquivalence( ),
                        inOrder );
            final Equivalence<boolean[ ]> arrayEquivalence =
                    ArrayEquivalences.forBooleanArrays( iterableEquivalence );
            return new TypeEquivalence<T>( (Equivalence<T>) arrayEquivalence, arrayType );
        }

        if ( !elementType.isPrimitive( ) )
        {
            final Equivalence<Iterable<? extends U>> iterableEquivalence =
                    equivalenceFactory.<U>createIterableEquivalence(
                        equivalenceOnElements.getEquivalence( ),
                        inOrder );
            final Equivalence<U[ ]> arrayEquivalence =
                    ArrayEquivalences.forArrays( iterableEquivalence );
            return new TypeEquivalence<T>( (Equivalence<T>) arrayEquivalence, arrayType );
        }
        // TODO
        return null;
    }

    private <T, U> TypeEquivalence<T> toArray( final Equivalence<U> equivalence, final boolean inOrder,
            final Class<T> arrayType, final Function<Equivalence<Iterable<? extends U>>, Equivalence<?>> converter ) {
        final Equivalence<Iterable<? extends U>> iterableEquivalence =
                equivalenceFactory.<U>createIterableEquivalence(
                    equivalence,
                    inOrder );
        final Equivalence<T> arrayEquivalence =
                (Equivalence<T>) converter.apply( iterableEquivalence );
        return new TypeEquivalence<T>( arrayEquivalence, arrayType );

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

    @SuppressWarnings( "unchecked" )
    public static <T, U> Equivalence<U> primitiveArrayEquivalence( final Equivalence<T> equivalence,
            final boolean inOrder, final Class<T> componentType, final Class<U> arrayType ) {
        checkState( componentType.isPrimitive( ) && arrayType.getComponentType( ) == componentType );

        if ( componentType == boolean.class )
            return (Equivalence<U>) booleanArrayEquivalence( (Equivalence<Boolean>) equivalence, inOrder );
        else if ( componentType == byte.class )
            return (Equivalence<U>) byteArrayEquivalence( (Equivalence<Byte>) equivalence, inOrder );
        else if ( componentType == char.class )
            return (Equivalence<U>) charArrayEquivalence( (Equivalence<Character>) equivalence, inOrder );
        else if ( componentType == double.class )
            return (Equivalence<U>) doubleArrayEquivalence( (Equivalence<Double>) equivalence, inOrder );
        else if ( componentType == float.class )
            return (Equivalence<U>) floatArrayEquivalence( (Equivalence<Float>) equivalence, inOrder );
        else if ( componentType == int.class )
            return (Equivalence<U>) intArrayEquivalence( (Equivalence<Integer>) equivalence, inOrder );
        else if ( componentType == long.class )
            return (Equivalence<U>) longArrayEquivalence( (Equivalence<Long>) equivalence, inOrder );
        else if ( componentType == short.class )
            return (Equivalence<U>) shortArrayEquivalence( (Equivalence<Short>) equivalence, inOrder );

        throw new IllegalStateException( "Unreachable." );
    }

    private final EquivalenceFactory equivalenceFactory;
    private final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter;
}
