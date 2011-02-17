package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.primitives.Primitives.wrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.Text;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.annotations.VisibleForTesting;

final class EquivalenceAnnotationProcessor<T> {

    public static <T> EquivalenceAnnotationProcessor<T> annotationProcessorFor(
            @SuppressWarnings( "unused" ) final Class<T> sourceType,
            final Method specification,
            final Method target ) {
        return new EquivalenceAnnotationProcessor<T>(
            new LiftedEquivalenceFactory( ), new EquivalenceFactory( ), new AnnotationTypeReader( ),
            specification, target );
    }

    @VisibleForTesting
    EquivalenceAnnotationProcessor( final LiftedEquivalenceFactory liftedEquivalenceFactory,
            final EquivalenceFactory equivalenceFactory,
            final AnnotationTypeReader annotationTypeReader,
            final Method specification, final Method target ) {
        this.liftedEquivalenceFactory = liftedEquivalenceFactory;
        this.equivalenceFactory = equivalenceFactory;
        this.annotationTypeReader = annotationTypeReader;
        this.specification = specification;
        this.target = target;
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        final TypeEquivalence<?> typeEquivalence = computeEquivalenceOnPropertyType( );

        return lift( typeEquivalence );
    }

    private TypeEquivalence<?> computeEquivalenceOnPropertyType( ) {
        annotationType = annotationTypeReader.getEquivalenceAnnotationType( specification );

        if ( annotationType == ApproximateEquality.class )
            return computeApproximateEqualityEquivalence( );

        if ( annotationType == Text.class )
            return computeTextEquivalence( );

        return computeGenericEquivalence( propertyType( ) );
    }

    private TypeEquivalence<String> computeTextEquivalence( ) {
        final Equivalence<String> equivalence = equivalenceFactory
            .getTextEquivalence( getSpecificationAnnotation( Text.class ).options( ) );

        return new TypeEquivalence<String>( equivalence, String.class );
    }

    private TypeEquivalence<Number> computeApproximateEqualityEquivalence( ) {
        final Equivalence<Number> equivalence = equivalenceFactory
            .getApproximateEquality( getSpecificationAnnotation( ApproximateEquality.class ).tolerance( ) );

        return new TypeEquivalence<Number>( equivalence, Number.class );
    }

    private <U> Equivalence<T> lift( final TypeEquivalence<U> typeEquivalence ) {
        final Class<U> requiredPropertyType = typeEquivalence.getType( );
        checkArgument(
            requiredPropertyType.isAssignableFrom( propertyType( ) ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to %s.",
                specification, annotationType.getSimpleName( ), requiredPropertyType.getName( ) ) );

        return liftedEquivalenceFactory.create( specification.getName( ),
            typeEquivalence.getEquivalence( ),
            new ReadPropertyFunction<T, U>( target, requiredPropertyType ) );
    }

    private <V> TypeEquivalence<V> computeGenericEquivalence( final Class<V> type ) {

        final Equivalence<V> propertyEquivalence;

        if ( annotationType == ByEquivalence.class )
        {
            final ByEquivalence specificationAnnotation = getSpecificationAnnotation( ByEquivalence.class );
            propertyEquivalence = equivalenceFactory
                .createEquivalenceInstance( specificationAnnotation, specification, type );
        }
        else if ( annotationType == BySpecification.class )
        {
            final Class<?> subSpecification = getSpecificationAnnotation( BySpecification.class ).value( );
            propertyEquivalence =
                    equivalenceFactory.getEquivalenceFromSpecification( type, subSpecification );
        }
        else if ( annotationType == Identity.class && !specification.getReturnType( ).isPrimitive( ) )
        {
            propertyEquivalence = equivalenceFactory.getIdentity( );
        }
        else if ( annotationType == Equality.class || annotationType == Identity.class )
        {
            propertyEquivalence = equivalenceFactory.getEquality( );
        }
        else
        {
            throw new IllegalStateException( String.format( "Cannot process annotation of type %s.",
                annotationType.getSimpleName( ) ) );
        }

        return new TypeEquivalence<V>( propertyEquivalence, type );

    }

    private <A extends Annotation> A getSpecificationAnnotation( final Class<A> annotationClass ) {
        checkState( specification.isAnnotationPresent( annotationClass ) );
        return specification.getAnnotation( annotationClass );
    }

    private Class<?> propertyType( ) {
        return wrap( specification.getReturnType( ) );
    }

    private final Method specification;
    private final Method target;

    private Class<? extends Annotation> annotationType;

    private final LiftedEquivalenceFactory liftedEquivalenceFactory;
    private final AnnotationTypeReader annotationTypeReader;
    private final EquivalenceFactory equivalenceFactory;

    private static class TypeEquivalence<U> {

        public TypeEquivalence( final Equivalence<U> equivalence, final Class<U> type ) {
            this.equivalence = equivalence;
            this.type = type;
        }

        public Equivalence<U> getEquivalence( ) {
            return equivalence;
        }

        public Class<U> getType( ) {
            return type;
        }

        private final Equivalence<U> equivalence;
        private final Class<U> type;
    }

}
