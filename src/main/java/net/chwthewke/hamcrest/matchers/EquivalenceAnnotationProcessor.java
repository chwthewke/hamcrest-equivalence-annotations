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

        annotationType = annotationTypeReader.getEquivalenceAnnotationType( specification );

        if ( annotationType == ApproximateEquality.class )
            return computeApproximateEqualityEquivalence( );

        if ( annotationType == Text.class )
            return computeTextEquivalence( );

        return computeGenericEquivalence( propertyType( ) );
    }

    private Equivalence<T> computeTextEquivalence( ) {
        final Equivalence<String> equivalence = equivalenceFactory
            .getTextEquivalence( getSpecificationAnnotation( Text.class ).options( ) );

        return computeTypedEquivalence( String.class, equivalence );
    }

    private Equivalence<T> computeApproximateEqualityEquivalence( ) {
        final Equivalence<Number> equivalence = equivalenceFactory
            .getApproximateEquality( getSpecificationAnnotation( ApproximateEquality.class ).tolerance( ) );

        return computeTypedEquivalence( Number.class, equivalence );
    }

    private <U> Equivalence<T> computeTypedEquivalence( final Class<U> requiredPropertyType,
            final Equivalence<U> propertyEquivalence ) {
        checkArgument(
            requiredPropertyType.isAssignableFrom( propertyType( ) ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to %s.",
                specification, annotationType.getSimpleName( ), requiredPropertyType.getName( ) ) );

        return liftedEquivalenceFactory.create( specification.getName( ),
            propertyEquivalence,
            new ReadPropertyFunction<T, U>( target, requiredPropertyType ) );
    }

    private <V> Equivalence<T> computeGenericEquivalence( final Class<V> type ) {

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

        return liftedEquivalenceFactory.create( specification.getName( ),
            propertyEquivalence,
            new ReadPropertyFunction<T, V>( target, type ) );
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

}
