package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Functions.compose;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.primitives.Primitives.wrap;
import static net.chwthewke.hamcrest.matchers.LiftOperator.liftWith;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.base.Function;

final class EquivalenceAnnotationProcessor<T, V> {

    public static <T> EquivalenceAnnotationProcessor<T, ?> annotationProcessorFor(
                    final Class<T> sourceType,
                    final Method specification,
                    final Method target ) {
        return annotationProcessorFor( sourceType, wrap( specification.getReturnType( ) ), specification, target );
    }

    private static <T, V> EquivalenceAnnotationProcessor<T, V> annotationProcessorFor(
                    @SuppressWarnings( "unused" ) final Class<T> sourceType,
                    final Class<V> propertyType,
                    final Method specification,
                    final Method target ) {
        return new EquivalenceAnnotationProcessor<T, V>( propertyType, specification, target );
    }

    private EquivalenceAnnotationProcessor( final Class<V> propertyType, final Method specification, final Method target ) {
        this.propertyType = propertyType;
        this.specification = specification;
        this.target = target;
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        annotationType = annotationTypeReader.getEquivalenceAnnotationType( specification );

        if ( annotationType == ApproximateEquality.class )
        {
            return computeApproximateEqualityEquivalence( );
        }

        return computeGenericEquivalence( );
    }

    private Equivalence<T> computeApproximateEqualityEquivalence( ) {
        checkArgument(
            Number.class.isAssignableFrom( propertyType ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to java.lang.Number.",
                specification, ApproximateEquality.class.getSimpleName( ) ) );

        final LiftOperator<T, Double> liftOperator =
                liftWith( compose( toDouble, new ReadPropertyFunction<T, Number>( target, Number.class ) ) );

        final double tolerance = getSpecificationAnnotation( ApproximateEquality.class ).tolerance( );

        return liftOperator.liftEquivalence( specification.getName( ), equivalenceFactory.getApproximateEquality( tolerance ) );
    }

    private Equivalence<T> computeGenericEquivalence( ) {
        final LiftOperator<T, V> liftOperator = liftWith( new ReadPropertyFunction<T, V>( target, propertyType ) );

        final Equivalence<V> propertyEquivalence;

        if ( annotationType == ByEquivalence.class )
        {
            final ByEquivalence specificationAnnotation = getSpecificationAnnotation( ByEquivalence.class );
            propertyEquivalence = equivalenceActivator
                .createEquivalenceInstance( specificationAnnotation, specification, propertyType );
        }
        else if ( annotationType == BySpecification.class )
        {
            final Class<?> subSpecification = getSpecificationAnnotation( BySpecification.class ).value( );
            propertyEquivalence =
                    equivalenceFactory.getEquivalenceFromSpecification( propertyType, subSpecification );
        }
        else if ( annotationType == Identity.class && !specification.getReturnType( ).isPrimitive( ) )
        {
            propertyEquivalence = equivalenceFactory.getIdentity( );
        }
        else if ( annotationType == Identity.class || annotationType == Equality.class )
        {
            propertyEquivalence = equivalenceFactory.getEquality( );
        }
        else
        {
            throw new IllegalStateException( String.format( "Cannot process annotation %s of type %s.",
                getSpecificationAnnotation( annotationType ), annotationType.getSimpleName( ) ) );
        }

        return liftOperator.liftEquivalence( specification.getName( ), propertyEquivalence );
    }

    private <A extends Annotation> A getSpecificationAnnotation( final Class<A> annotationClass ) {
        checkState( specification.isAnnotationPresent( annotationClass ) );
        return specification.getAnnotation( annotationClass );
    }

    private final Method specification;
    private final Method target;

    private final Class<V> propertyType;
    private Class<? extends Annotation> annotationType;

    private final EquivalenceActivator equivalenceActivator = new EquivalenceActivator( );
    private final AnnotationTypeReader annotationTypeReader = new AnnotationTypeReader( );
    private final EquivalenceFactory equivalenceFactory = new EquivalenceFactory( );

    private static final Function<Number, Double> toDouble =
            new Function<Number, Double>( ) {
                public Double apply( final Number input ) {
                    return input == null ? null : input.doubleValue( );
                }
            };

}
