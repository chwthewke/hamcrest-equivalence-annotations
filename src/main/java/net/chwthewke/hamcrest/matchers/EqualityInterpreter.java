package net.chwthewke.hamcrest.matchers;


import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;


final class EqualityInterpreter<U> implements
        EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        return new EqualityEquivalence<U>( );
    }
}
