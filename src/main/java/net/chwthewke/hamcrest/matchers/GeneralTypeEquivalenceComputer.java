package net.chwthewke.hamcrest.matchers;

import static com.google.common.primitives.Primitives.wrap;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.Text;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class GeneralTypeEquivalenceComputer implements TypeEquivalenceComputer {

    GeneralTypeEquivalenceComputer( final EquivalenceFactory equivalenceFactory ) {
        this.equivalenceFactory = equivalenceFactory;
    }

    public TypeEquivalence<?> computeEquivalenceOnPropertyType( final Annotation equivalenceAnnotation,
            final Class<?> propertyType ) {

        if ( equivalenceAnnotation instanceof ApproximateEquality )
            return computeApproximateEqualityEquivalence( (ApproximateEquality) equivalenceAnnotation );

        if ( equivalenceAnnotation instanceof Text )
            return computeTextEquivalence( (Text) equivalenceAnnotation );

        return computeGenericEquivalence( equivalenceAnnotation, wrap( propertyType ), propertyType.isPrimitive( ) );
    }

    private TypeEquivalence<String> computeTextEquivalence( final Text annotation ) {
        final Equivalence<String> equivalence = equivalenceFactory
            .getTextEquivalence( annotation.options( ) );

        return new TypeEquivalence<String>( equivalence, String.class );
    }

    private TypeEquivalence<Number> computeApproximateEqualityEquivalence( final ApproximateEquality annotation ) {
        final Equivalence<Number> equivalence = equivalenceFactory
            .getApproximateEquality( annotation.tolerance( ) );

        return new TypeEquivalence<Number>( equivalence, Number.class );
    }

    private <V> TypeEquivalence<V> computeGenericEquivalence( final Annotation equivalenceAnnotation,
            final Class<V> type, final boolean isPrimitive ) {

        final Equivalence<V> propertyEquivalence = computePropertyEquivalence( equivalenceAnnotation, type, isPrimitive );

        return new TypeEquivalence<V>( propertyEquivalence, type );

    }

    private <V> Equivalence<V> computePropertyEquivalence( final Annotation equivalenceAnnotation, final Class<V> type,
            final boolean isPrimitive ) {

        if ( equivalenceAnnotation instanceof ByEquivalence )
            return equivalenceFactory
                .createEquivalenceInstance( (ByEquivalence) equivalenceAnnotation, type );

        if ( equivalenceAnnotation instanceof BySpecification )
            return equivalenceFactory.getEquivalenceBySpecification( (BySpecification) equivalenceAnnotation, type );

        if ( equivalenceAnnotation instanceof Identity && !isPrimitive )
            return equivalenceFactory.getIdentity( );

        if ( equivalenceAnnotation instanceof Equality || equivalenceAnnotation instanceof Identity )
            return equivalenceFactory.getEquality( );

        throw new IllegalStateException( String.format( "Cannot process annotation of type %s.",
                equivalenceAnnotation.annotationType( ).getSimpleName( ) ) );
    }

    private final EquivalenceFactory equivalenceFactory;

}
