package net.chwthewke.hamcrest.matchers;

import java.lang.annotation.Annotation;

class SimpleTypeEquivalenceComputer extends CoreTypeEquivalenceComputer {

    SimpleTypeEquivalenceComputer( final EquivalenceFactory equivalenceFactory ) {
        super( equivalenceFactory );
    }

    public TypeEquivalence<?> computeEquivalenceOnPropertyType( final Annotation equivalenceAnnotation,
            final Class<?> propertyType ) {

        return computeCoreAnnotationEquivalence( equivalenceAnnotation, propertyType );
    }

}
