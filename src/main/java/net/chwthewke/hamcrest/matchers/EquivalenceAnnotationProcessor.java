package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.primitives.Primitives.wrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.annotations.VisibleForTesting;

final class EquivalenceAnnotationProcessor<T> {

    public static <T> EquivalenceAnnotationProcessor<T> annotationProcessorFor(
            @SuppressWarnings( "unused" ) final Class<T> sourceType,
            final Method specification,
            final Method target ) {
        return new EquivalenceAnnotationProcessor<T>(
            new SimpleTypeEquivalenceComputer( new EquivalenceFactory( ) ),
            new LiftedEquivalenceFactory( ), new AnnotationTypeReader( ),
            specification, target );
    }

    @VisibleForTesting
    EquivalenceAnnotationProcessor(
            final TypeEquivalenceComputer typeEquivalenceComputer,
            final LiftedEquivalenceFactory liftedEquivalenceFactory,
            final AnnotationTypeReader annotationTypeReader,
            final Method specification, final Method target ) {
        this.typeEquivalenceComputer = typeEquivalenceComputer;
        this.liftedEquivalenceFactory = liftedEquivalenceFactory;
        this.specification = specification;
        this.target = target;

        equivalenceAnnotation = checkNotNull( annotationTypeReader.getEquivalenceAnnotation( specification ),
            "Unexpected missing annotation." );
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        final TypeEquivalence<?> equivalenceOnPropertyType = typeEquivalenceComputer
            .computeEquivalenceOnPropertyType( equivalenceAnnotation, specification.getReturnType( ) );
        return lift( equivalenceOnPropertyType );
    }

    private <U> Equivalence<T> lift( final TypeEquivalence<U> typeEquivalence ) {
        final Class<U> requiredPropertyType = typeEquivalence.getType( );
        checkArgument(
            requiredPropertyType.isAssignableFrom( wrap( specification.getReturnType( ) ) ),
            String.format(
                "The equivalence specification property %s bears %s, so it must have a type assignable to %s.",
                specification, getAnnotationType( ).getSimpleName( ), requiredPropertyType.getName( ) ) );

        return liftedEquivalenceFactory.create( specification.getName( ),
            typeEquivalence.getEquivalence( ),
            new ReadPropertyFunction<T, U>( target, requiredPropertyType ) );
    }

    private Class<? extends Annotation> getAnnotationType( ) {
        return equivalenceAnnotation.annotationType( );
    }

    private final Method specification;
    private final Method target;

    private final LiftedEquivalenceFactory liftedEquivalenceFactory;
    private final Annotation equivalenceAnnotation;

    private final TypeEquivalenceComputer typeEquivalenceComputer;

}
