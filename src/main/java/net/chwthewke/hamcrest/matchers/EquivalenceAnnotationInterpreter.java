package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.Equivalence;


public interface EquivalenceAnnotationInterpreter<T> {

    Equivalence<T> interpretAnnotation( Method specificationMethod, Class<T> propertyType );

}
