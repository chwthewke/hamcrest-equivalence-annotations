package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IdentityEquivalence;


final class IdentityInterpreter<U> implements
        EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( Identity.class ) );

        return new IdentityEquivalence<U>( );
    }
}
