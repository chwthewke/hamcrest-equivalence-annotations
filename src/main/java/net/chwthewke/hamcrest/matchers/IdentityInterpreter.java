package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.Identity;

import org.hamcrest.Matcher;

final class IdentityInterpreter<U> implements
        EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( Identity.class ) );

        return new Equivalence<U>( ) {
            public Matcher<U> equivalentTo( final U expected ) {
                return sameInstance( expected );
            }
        };
    }
}
