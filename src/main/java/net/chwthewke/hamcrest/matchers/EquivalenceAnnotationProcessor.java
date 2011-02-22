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

        final TypeEquivalenceComputer typeEquivalenceComputer =
                new TypeEquivalenceComputer(
                    equivalenceFactory,
                    new BasicTypeEquivalenceComputer( equivalenceFactory ),
                    new AnnotationTypeReader( ) );

        final LiftedEquivalenceFactory liftedEquivalenceFactory = new LiftedEquivalenceFactory( );

        return new EquivalenceAnnotationProcessor<T>(
            typeEquivalenceComputer,
            liftedEquivalenceFactory,
            specification,
            target );
    }

    @VisibleForTesting
    EquivalenceAnnotationProcessor(
            final TypeEquivalenceComputer typeEquivalenceComputer,
            final LiftedEquivalenceFactory liftedEquivalenceFactory,
            final Method specification, final Method target ) {
        this.typeEquivalenceComputer = typeEquivalenceComputer;
        this.liftedEquivalenceFactory = liftedEquivalenceFactory;
        this.specification = specification;
        this.target = target;

    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        final TypeEquivalence<?> equivalenceOnPropertyType = typeEquivalenceComputer
            .computeEquivalenceOnPropertyType( specification );
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

    private final TypeEquivalenceComputer typeEquivalenceComputer;

}
