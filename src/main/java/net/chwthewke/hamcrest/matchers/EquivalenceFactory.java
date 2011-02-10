package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.equivalence.ApproximateEqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IdentityEquivalence;
import net.chwthewke.hamcrest.equivalence.TextEquivalence;
import net.chwthewke.hamcrest.equivalence.TextEquivalenceOption;

class EquivalenceFactory {

    public Equivalence<Number> getApproximateEquality( final double tolerance ) {
        return new ApproximateEqualityEquivalence( tolerance );
    }

    public <T> Equivalence<T> getEquality( ) {
        return new EqualityEquivalence<T>( );
    }

    public Equivalence<String> getTextEquivalence( final TextEquivalenceOption[ ] options ) {
        return TextEquivalence.textEquivalenceWith( options );
    }

    public <T> Equivalence<T> getIdentity( ) {
        return new IdentityEquivalence<T>( );
    }

    public <T> Equivalence<T> getEquivalenceFromSpecification( final Class<T> targetType, final Class<?> specification ) {
        return new CompositeEquivalence<T>( propertyFinder, specificationValidator, targetType, specification );
    }

    public <T> Equivalence<T> createEquivalenceInstance( final ByEquivalence specificationAnnotation,
            final Method specification,
            final Class<?> propertyType ) {
        return equivalenceActivator.createEquivalenceInstance( specificationAnnotation, specification, propertyType );
    }

    private final PropertyFinder propertyFinder = new PropertyFinder( );
    private final EquivalenceSpecificationValidator specificationValidator = new EquivalenceSpecificationValidator( );
    private final EquivalenceActivator equivalenceActivator = new EquivalenceActivator( );
}
