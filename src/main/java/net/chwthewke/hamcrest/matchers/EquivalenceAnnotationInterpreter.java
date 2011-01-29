package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

interface EquivalenceAnnotationInterpreter<T> {

    Equivalence<T> interpretAnnotation( Method specificationMethod, Class<T> propertyType );

}
