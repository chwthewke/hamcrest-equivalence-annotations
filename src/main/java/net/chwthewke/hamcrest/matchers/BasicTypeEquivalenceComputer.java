package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.primitives.Primitives.wrap;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.Text;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class BasicTypeEquivalenceComputer {

    BasicTypeEquivalenceComputer( final EquivalenceFactory equivalenceFactory ) {
        this.equivalenceFactory = equivalenceFactory;
    }

    public <T> TypeEquivalence<? super T> computeEquivalenceOnBasicType( final Annotation equivalenceAnnotation,
            final Class<T> propertyType ) {

        checkNotNull( equivalenceAnnotation, "Unexpected missing annotation." );

        if ( equivalenceAnnotation instanceof ApproximateEquality )
        {
            return computeApproximateEquality( (ApproximateEquality) equivalenceAnnotation, propertyType );
        }

        if ( equivalenceAnnotation instanceof Text )
        {
            return computeTextEquivalence( (Text) equivalenceAnnotation, propertyType );
        }

        return computeGenericEquivalence( equivalenceAnnotation, wrap( propertyType ), propertyType.isPrimitive( ) );
    }

    @SuppressWarnings( "unchecked" )
    private <T> TypeEquivalence<? super T> computeApproximateEquality( final ApproximateEquality equivalenceAnnotation,
            final Class<T> propertyType ) {
        checkState( Number.class.isAssignableFrom( wrap( propertyType ) ) );

        final Equivalence<Number> equivalence =
                equivalenceFactory.getApproximateEquality( ( equivalenceAnnotation ).tolerance( ) );

        return (TypeEquivalence<? super T>) new TypeEquivalence<Number>( equivalence, Number.class );
    }

    @SuppressWarnings( "unchecked" )
    private <T> TypeEquivalence<? super T> computeTextEquivalence( final Text annotation,
            final Class<T> propertyType ) {
        checkState( String.class.isAssignableFrom( wrap( propertyType ) ) );

        final Equivalence<String> equivalence = equivalenceFactory
            .getTextEquivalence( annotation.options( ) );

        return (TypeEquivalence<? super T>) new TypeEquivalence<String>( equivalence, String.class );
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
        {
            final Class<?> specificationInterface = ( (BySpecification) equivalenceAnnotation ).value( );
            return equivalenceFactory.getEquivalenceBySpecification( specificationInterface, type );
        }

        if ( equivalenceAnnotation instanceof Identity && !isPrimitive )
            return equivalenceFactory.getIdentity( );

        if ( equivalenceAnnotation instanceof Equality || equivalenceAnnotation instanceof Identity )
            return equivalenceFactory.getEquality( );

        throw new IllegalStateException( String.format( "Cannot process annotation of type %s.",
            equivalenceAnnotation.annotationType( ).getSimpleName( ) ) );
    }

    private final EquivalenceFactory equivalenceFactory;

}
