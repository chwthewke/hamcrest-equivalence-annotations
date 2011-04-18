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

class BasicTypeEquivalenceInterpreter {

    BasicTypeEquivalenceInterpreter( final EquivalenceFactory equivalenceFactory ) {
        this.equivalenceFactory = equivalenceFactory;
    }

    public <T> Equivalence<? super T> getEquivalenceFor( final Annotation equivalenceAnnotation,
            final Class<T> propertyType ) {

        checkNotNull( equivalenceAnnotation, "Unexpected missing annotation." );

        if ( equivalenceAnnotation.annotationType( ) == ApproximateEquality.class )
            return computeApproximateEquality( (ApproximateEquality) equivalenceAnnotation, propertyType );

        if ( equivalenceAnnotation.annotationType( ) == Text.class )
            return computeTextEquivalence( (Text) equivalenceAnnotation, propertyType );

        return computeGenericEquivalence( equivalenceAnnotation, wrap( propertyType ), propertyType.isPrimitive( ) );
    }

    @SuppressWarnings( "unchecked" )
    private <T> Equivalence<? super T> computeApproximateEquality( final ApproximateEquality equivalenceAnnotation,
            final Class<T> propertyType ) {
        checkState( Number.class.isAssignableFrom( wrap( propertyType ) ) );

        return (Equivalence<? super T>) equivalenceFactory
            .getApproximateEquality( ( equivalenceAnnotation ).tolerance( ) );
    }

    @SuppressWarnings( "unchecked" )
    private <T> Equivalence<? super T> computeTextEquivalence( final Text annotation,
            final Class<T> propertyType ) {
        checkState( String.class.isAssignableFrom( wrap( propertyType ) ) );

        return (Equivalence<? super T>) equivalenceFactory
            .getTextEquivalence( annotation.options( ) );

    }

    private <V> Equivalence<V> computeGenericEquivalence( final Annotation equivalenceAnnotation,
            final Class<V> type, final boolean isPrimitive ) {

        final Class<? extends Annotation> annotationType = equivalenceAnnotation.annotationType( );

        if ( annotationType == ByEquivalence.class )
            return equivalenceFactory
                .createEquivalenceInstance( (ByEquivalence) equivalenceAnnotation, type );

        if ( annotationType == BySpecification.class )
        {
            final Class<?> specificationInterface = ( (BySpecification) equivalenceAnnotation ).value( );
            return equivalenceFactory.getEquivalenceBySpecification( specificationInterface, type );
        }

        if ( annotationType == Identity.class && !isPrimitive )
            return equivalenceFactory.getIdentity( );

        if ( annotationType == Equality.class || annotationType == Identity.class )
            return equivalenceFactory.getEquality( );

        throw new IllegalStateException( String.format( "Cannot process annotation of type %s.",
            annotationType.getSimpleName( ) ) );

    }

    private final EquivalenceFactory equivalenceFactory;

}
