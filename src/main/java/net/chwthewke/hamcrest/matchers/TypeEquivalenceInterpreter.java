package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.primitives.Primitives.wrap;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.arrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.booleanArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.byteArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.charArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.doubleArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.floatArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.intArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.longArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.shortArrayEquivalence;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class TypeEquivalenceInterpreter {

    TypeEquivalenceInterpreter(
            final EquivalenceFactory equivalenceFactory,
            final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter ) {
        this.equivalenceFactory = equivalenceFactory;
        this.basicTypeEquivalenceInterpreter = basicTypeEquivalenceInterpreter;
    }

    public <T> Equivalence<? super T> getEquivalenceFor(
            final TypeEquivalenceSpecification<T> specification ) {

        final Annotation equivalenceAnnotation = specification.getEquivalenceAnnotation( );

        final Class<T> targetType = specification.getTargetType( );

        if ( !specification.hasContainerAnnotation( ) )
            return interpretEquivalenceOnBasicType( equivalenceAnnotation, targetType );

        final Annotation containerAnnotation = specification.getContainerAnnotation( );
        if ( containerAnnotation.annotationType( ) == OnIterableElements.class )
        {
            final OnIterableElements onIterableAnnotation = (OnIterableElements) containerAnnotation;

            final boolean inOrder = onIterableAnnotation.inOrder( );
            final Class<?> elementType = onIterableAnnotation.elementType( );

            return createIterableEquivalence( equivalenceAnnotation,
                inOrder, targetType, elementType );
        }

        if ( containerAnnotation.annotationType( ) == OnArrayElements.class )
        {
            final OnArrayElements onArrayAnnotation = (OnArrayElements) containerAnnotation;

            final boolean inOrder = onArrayAnnotation.inOrder( );

            final Class<?> elementType = targetType.getComponentType( );

            return createArrayEquivalence( equivalenceAnnotation,
                inOrder, targetType, elementType );
        }

        throw new IllegalArgumentException(
            String.format( "Unknown container annotation %s", containerAnnotation.annotationType( ).getSimpleName( ) ) );
    }

    private <T, U> Equivalence<? super T> createIterableEquivalence( final Annotation equivalenceAnnotation,
            final boolean inOrder, final Class<T> iterableType, final Class<U> elementType ) {
        checkArgument( Iterable.class.isAssignableFrom( iterableType ),
            "'propertyType' must be a subtype of Iterable." );

        final Equivalence<? super U> equivalenceOnElements =
                interpretEquivalenceOnBasicType( equivalenceAnnotation, elementType );

        final Equivalence<Iterable<? extends U>> iterableEquivalence =
                equivalenceFactory.<U>createIterableEquivalence(
                    equivalenceOnElements,
                    inOrder );

        return unckechedTypeEquivalence( iterableEquivalence );
    }

    private <T, U> Equivalence<? super T> createArrayEquivalence( final Annotation equivalenceAnnotation,
            final boolean inOrder, final Class<T> arrayType, final Class<U> elementType ) {

        checkArgument( arrayType.isArray( ), "'specification.getTargetType( )' must be an array type." );

        final Equivalence<? super U> equivalenceOnElements =
                interpretEquivalenceOnBasicType( equivalenceAnnotation, wrap( elementType ) );

        if ( elementType.isPrimitive( ) )
            return primitiveArrayEquivalence( equivalenceOnElements, inOrder, arrayType, elementType );

        return unckechedTypeEquivalence( arrayEquivalence( equivalenceOnElements, inOrder ) );
    }

    private <T> Equivalence<? super T> interpretEquivalenceOnBasicType( final Annotation equivalenceAnnotation,
            final Class<T> propertyType ) {
        return basicTypeEquivalenceInterpreter.getEquivalenceFor( equivalenceAnnotation, propertyType );
    }

    @SuppressWarnings( "unchecked" )
    private <T, U> Equivalence<T> unckechedTypeEquivalence( final Equivalence<U> equivalence ) {
        return (Equivalence<T>) equivalence;
    }

    @SuppressWarnings( "unchecked" )
    public static <U, V> Equivalence<V> primitiveArrayEquivalence( final Equivalence<? super U> equivalence,
            final boolean inOrder, final Class<V> arrayType, final Class<U> componentType ) {
        checkState( componentType.isPrimitive( ) && arrayType.getComponentType( ) == componentType );

        if ( componentType == boolean.class )
            return (Equivalence<V>) booleanArrayEquivalence( (Equivalence<Boolean>) equivalence, inOrder );
        else if ( componentType == byte.class )
            return (Equivalence<V>) byteArrayEquivalence( (Equivalence<Byte>) equivalence, inOrder );
        else if ( componentType == char.class )
            return (Equivalence<V>) charArrayEquivalence( (Equivalence<Character>) equivalence, inOrder );
        else if ( componentType == double.class )
            return (Equivalence<V>) doubleArrayEquivalence( (Equivalence<Double>) equivalence, inOrder );
        else if ( componentType == float.class )
            return (Equivalence<V>) floatArrayEquivalence( (Equivalence<Float>) equivalence, inOrder );
        else if ( componentType == int.class )
            return (Equivalence<V>) intArrayEquivalence( (Equivalence<Integer>) equivalence, inOrder );
        else if ( componentType == long.class )
            return (Equivalence<V>) longArrayEquivalence( (Equivalence<Long>) equivalence, inOrder );
        else if ( componentType == short.class )
            return (Equivalence<V>) shortArrayEquivalence( (Equivalence<Short>) equivalence, inOrder );

        throw new IllegalStateException( "Unreachable." );
    }

    private final EquivalenceFactory equivalenceFactory;
    private final BasicTypeEquivalenceInterpreter basicTypeEquivalenceInterpreter;
}
