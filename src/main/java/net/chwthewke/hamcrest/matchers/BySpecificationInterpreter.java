package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.equivalence.Equivalence;

final class BySpecificationInterpreter<U> implements
        EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( BySpecification.class ) );

        final BySpecification annotation = specificationMethod.getAnnotation( BySpecification.class );

        // TODO redo injection
        return new CompositeMatcherFactory<U>(
                    new PropertyFinder( ),
                    new EquivalenceSpecificationValidator( ),
                    new EquivalenceAnnotationReader( new EquivalenceAnnotationInterpreters( ) ),
                    propertyType,
                    annotation.value( ) );
    }
}
