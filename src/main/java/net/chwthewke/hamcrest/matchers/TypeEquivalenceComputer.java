package net.chwthewke.hamcrest.matchers;

import java.lang.annotation.Annotation;

interface TypeEquivalenceComputer {

    // TODO change sig to <V> TypeEquivalence<? super V> computeEquivalenceOnPropertyType( Annotation, Class<V> )
    TypeEquivalence<?> computeEquivalenceOnPropertyType( final Annotation equivalenceAnnotation,
            final Class<?> propertyType );

}
