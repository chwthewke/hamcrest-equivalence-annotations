package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.primitives.Primitives.wrap;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import com.google.common.annotations.VisibleForTesting;

final class EquivalenceAnnotationProcessor<T> {

    public static <T> EquivalenceAnnotationProcessor<T> annotationProcessorFor(
            @SuppressWarnings( "unused" ) final Class<T> sourceType,
            final Method specification,
            final Method target ) {

        final EquivalenceFactory equivalenceFactory = new EquivalenceFactory( );

        final TypeEquivalenceInterpreter typeEquivalenceInterpreter =
                new TypeEquivalenceInterpreter(
                    equivalenceFactory,
                    new BasicTypeEquivalenceInterpreter( equivalenceFactory ) );

        final LiftedEquivalenceFactory liftedEquivalenceFactory = new LiftedEquivalenceFactory( );

        final AnnotationReader annotationReader = new AnnotationReader( );

        return new EquivalenceAnnotationProcessor<T>(
            annotationReader,
            typeEquivalenceInterpreter,
            liftedEquivalenceFactory,
            specification,
            target );
    }

    @VisibleForTesting
    EquivalenceAnnotationProcessor(
            final AnnotationReader annotationReader,
            final TypeEquivalenceInterpreter typeEquivalenceInterpreter,
            final LiftedEquivalenceFactory liftedEquivalenceFactory,
            final Method specification, final Method target ) {
        this.annotationReader = annotationReader;
        this.typeEquivalenceInterpreter = typeEquivalenceInterpreter;
        this.liftedEquivalenceFactory = liftedEquivalenceFactory;
        this.specification = specification;
        this.target = target;

    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        final TypeEquivalence<?> equivalenceOnPropertyType = typeEquivalenceInterpreter
            .getEquivalenceFor( annotationReader.getTypeEquivalenceSpecification( specification ) );
        return lift( equivalenceOnPropertyType );
    }

    private <U> Equivalence<T> lift( final TypeEquivalence<U> typeEquivalence ) {
        final Class<U> requiredPropertyType = typeEquivalence.getType( );
        checkArgument(
            requiredPropertyType.isAssignableFrom( wrap( specification.getReturnType( ) ) ),
            String.format(
                "The equivalence specification property %s must have a type assignable to %s.",
                specification, requiredPropertyType.getName( ) ) );

        return liftedEquivalenceFactory.create( specification.getName( ),
            typeEquivalence.getEquivalence( ),
            new ReadPropertyFunction<T, U>( target, requiredPropertyType ) );
    }

    private final Method specification;
    private final Method target;

    private final LiftedEquivalenceFactory liftedEquivalenceFactory;
    private final AnnotationReader annotationReader;

    private final TypeEquivalenceInterpreter typeEquivalenceInterpreter;

}
