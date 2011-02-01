package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.equivalence.ApproximateEqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;


final class ApproximateEqualityInterpreter implements
        EquivalenceAnnotationInterpreter<Double> {
    public Equivalence<Double> interpretAnnotation( final Method specificationMethod,
            final Class<Double> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( ApproximateEquality.class ) );

        return new ApproximateEqualityEquivalence( specificationMethod.getAnnotation( ApproximateEquality.class ).tolerance( ) );
    }
}
