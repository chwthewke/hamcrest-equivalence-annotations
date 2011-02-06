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
import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.annotations.VisibleForTesting;

final class EquivalenceAnnotationProcessor<T> {

    public static <T> EquivalenceAnnotationProcessor<T> annotationProcessorFor(
                    @SuppressWarnings( "unused" ) final Class<T> sourceType,
                    final Method specification,
                    final Method target ) {
        return new EquivalenceAnnotationProcessor<T>( new LiftedEquivalenceFactory( ), specification, target );
    }

    @VisibleForTesting
    EquivalenceAnnotationProcessor( final LiftedEquivalenceFactory liftedEquivalenceFactory,
            final Method specification, final Method target ) {
        this.liftedEquivalenceFactory = liftedEquivalenceFactory;
        this.specification = specification;
        this.target = target;
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        annotationType = annotationTypeReader.getEquivalenceAnnotationType( specification );

        if ( annotationType == ApproximateEquality.class )
        {
            return computeApproximateEqualityEquivalence( );
        }

        return computeGenericEquivalence( propertyType( ) );
    }

    private Equivalence<T> computeApproximateEqualityEquivalence( ) {
        checkArgument(
            Number.class.isAssignableFrom( propertyType( ) ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to java.lang.Number.",
                specification, ApproximateEquality.class.getSimpleName( ) ) );

        final double tolerance = getSpecificationAnnotation( ApproximateEquality.class ).tolerance( );

        return liftedEquivalenceFactory.create( specification.getName( ),
            new ReadPropertyFunction<T, Number>( target, Number.class ),
            equivalenceFactory.getApproximateEquality( tolerance ) );
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
            throw new IllegalStateException( String.format( "Cannot process annotation %s of type %s.",
                getSpecificationAnnotation( annotationType ), annotationType.getSimpleName( ) ) );
        }

        return liftedEquivalenceFactory.create( specification.getName( ),
            new ReadPropertyFunction<T, V>( target, type ),
            propertyEquivalence );
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
    private final AnnotationTypeReader annotationTypeReader = new AnnotationTypeReader( );
    private final EquivalenceFactory equivalenceFactory = new EquivalenceFactory( );

}
