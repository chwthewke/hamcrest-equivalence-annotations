package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;

final class EqualityInterpreter<U> implements
        EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        return new Equivalence<U>( ) {
            public Matcher<U> equivalentTo( final U expected ) {
                return equalTo( expected );
            }
        };
    }
}
