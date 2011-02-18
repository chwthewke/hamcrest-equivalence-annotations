package net.chwthewke.hamcrest.matchers;

import java.lang.annotation.Annotation;

interface TypeEquivalenceComputer {

    TypeEquivalence<?> computeEquivalenceOnPropertyType( final Annotation equivalenceAnnotation,
            final Class<?> propertyType );

}
