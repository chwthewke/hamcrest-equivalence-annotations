package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.equivalence.NullAwareEquivalence.nullAware;
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

    public <T> Equivalence<T> getEquivalenceBySpecification( final Class<?> specificationInterface,
            final Class<T> targetType ) {
        return nullAware(
            new CompositeEquivalence<T>( propertyFinder, specificationValidator, targetType, specificationInterface ),
            targetType );
    }

    public <T> Equivalence<T> createEquivalenceInstance( final ByEquivalence specificationAnnotation,
            final Class<?> propertyType ) {
        return equivalenceActivator.createEquivalenceInstance( specificationAnnotation, propertyType );
    }

    private final PropertyFinder propertyFinder = new PropertyFinder( );
    private final EquivalenceSpecificationValidator specificationValidator = new EquivalenceSpecificationValidator( );
    private final EquivalenceActivator equivalenceActivator = new EquivalenceActivator( );
}
