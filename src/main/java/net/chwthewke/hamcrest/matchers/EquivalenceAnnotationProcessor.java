package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
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
        this.specification = specification;
        this.target = target;

        equivalenceAnnotation = checkNotNull( annotationTypeReader.getEquivalenceAnnotation( specification ),
            "Unexpected missing annotation." );
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        return lift( computeEquivalenceOnPropertyType( ) );
    }

    private TypeEquivalence<?> computeEquivalenceOnPropertyType( ) {

        if ( equivalenceAnnotation instanceof ApproximateEquality )
            return computeApproximateEqualityEquivalence( (ApproximateEquality) equivalenceAnnotation );

        if ( equivalenceAnnotation instanceof Text )
            return computeTextEquivalence( (Text) equivalenceAnnotation );

        return computeGenericEquivalence( propertyType( ) );
    }

    private TypeEquivalence<String> computeTextEquivalence( final Text annotation ) {
        final Equivalence<String> equivalence = equivalenceFactory
            .getTextEquivalence( annotation.options( ) );

        return new TypeEquivalence<String>( equivalence, String.class );
    }

    private TypeEquivalence<Number> computeApproximateEqualityEquivalence( final ApproximateEquality annotation ) {
        final Equivalence<Number> equivalence = equivalenceFactory
            .getApproximateEquality( annotation.tolerance( ) );

        return new TypeEquivalence<Number>( equivalence, Number.class );
    }

    private <U> Equivalence<T> lift( final TypeEquivalence<U> typeEquivalence ) {
        final Class<U> requiredPropertyType = typeEquivalence.getType( );
        checkArgument(
            requiredPropertyType.isAssignableFrom( propertyType( ) ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to %s.",
                specification, getAnnotationType( ).getSimpleName( ), requiredPropertyType.getName( ) ) );

        return liftedEquivalenceFactory.create( specification.getName( ),
            typeEquivalence.getEquivalence( ),
            new ReadPropertyFunction<T, U>( target, requiredPropertyType ) );
    }

    private <V> TypeEquivalence<V> computeGenericEquivalence( final Class<V> type ) {

        final Equivalence<V> propertyEquivalence = computePropertyEquivalence( type );

        return new TypeEquivalence<V>( propertyEquivalence, type );

    }

    private <V> Equivalence<V> computePropertyEquivalence( final Class<V> type ) {

        if ( equivalenceAnnotation instanceof ByEquivalence )
            return equivalenceFactory
                .createEquivalenceInstance( (ByEquivalence) equivalenceAnnotation, specification, type );

        if ( equivalenceAnnotation instanceof BySpecification )
            return equivalenceFactory.getEquivalenceBySpecification( type, (BySpecification) equivalenceAnnotation );

        if ( equivalenceAnnotation instanceof Identity && !specification.getReturnType( ).isPrimitive( ) )
            return equivalenceFactory.getIdentity( );

        if ( equivalenceAnnotation instanceof Equality || equivalenceAnnotation instanceof Identity )
            return equivalenceFactory.getEquality( );

        throw new IllegalStateException( String.format( "Cannot process annotation of type %s.",
                getAnnotationType( ).getSimpleName( ) ) );
    }

    private Class<?> propertyType( ) {
        return wrap( specification.getReturnType( ) );
    }

    private Class<? extends Annotation> getAnnotationType( ) {
        return equivalenceAnnotation.annotationType( );
    }

    private final Method specification;
    private final Method target;

    private final LiftedEquivalenceFactory liftedEquivalenceFactory;
    private final EquivalenceFactory equivalenceFactory;
    private final Annotation equivalenceAnnotation;

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
