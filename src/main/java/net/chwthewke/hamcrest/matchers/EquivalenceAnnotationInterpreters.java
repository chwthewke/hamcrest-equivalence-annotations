package net.chwthewke.hamcrest.matchers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

class EquivalenceAnnotationInterpreters {

    public EquivalenceAnnotationInterpreter<Double> approximateEqualityInterpreter( ) {
        return new ApproximateEqualityInterpreter( );
    }

    public <U> EquivalenceAnnotationInterpreter<U> selectAnnotationInterpreter(
            final Class<? extends Annotation> equivalenceAnnotationType, final Method specificationMethod ) {

        if ( equivalenceAnnotationType == Equality.class )
        {
            return new EqualityInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == Identity.class )
        {
            return specificationMethod.getReturnType( ).isPrimitive( )
                    ? new EqualityInterpreter<U>( )
                    : new IdentityInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == BySpecification.class )
        {
            return new BySpecificationInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == ByEquivalence.class )
        {
            return new ByEquivalenceInterpreter<U>( );
        }

        throw new IllegalArgumentException(
            String.format( "Unknown equivalence annotation %s", equivalenceAnnotationType.getName( ) ) );
    }
}
