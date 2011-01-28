package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;
import static org.hamcrest.Matchers.closeTo;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;

import org.hamcrest.Matcher;

final class ApproximateEqualityInterpreter implements
        EquivalenceAnnotationInterpreter<Double> {
    public Equivalence<Double> interpretAnnotation( final Method specificationMethod,
            final Class<Double> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( ApproximateEquality.class ) );

        final ApproximateEquality annotation = specificationMethod.getAnnotation( ApproximateEquality.class );

        return new Equivalence<Double>( ) {
            public Matcher<Double> equivalentTo( final Double expected ) {
                return closeTo( expected, annotation.tolerance( ) );
            }
        };
    }
}
