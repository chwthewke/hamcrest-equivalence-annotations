package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.equivalence.Equivalence;

class TypeEquivalenceComputer {

    TypeEquivalenceComputer(
            final EquivalenceFactory equivalenceFactory,
            final BasicTypeEquivalenceComputer basicTypeEquivalenceComputer,
            final AnnotationTypeReader annotationTypeReader ) {
        this.equivalenceFactory = equivalenceFactory;
        this.basicTypeEquivalenceComputer = basicTypeEquivalenceComputer;
        this.annotationTypeReader = annotationTypeReader;
    }

    // TODO refactor to decouple from "Method" (pass type and 'set' of annotations)
    public TypeEquivalence<?> computeEquivalenceOnPropertyType( final Method specification ) {
        final Annotation equivalenceAnnotation =
                checkNotNull( annotationTypeReader.getEquivalenceAnnotation( specification ),
                    "Unexpected missing annotation." );

        final Class<?> propertyType = specification.getReturnType( );
        if ( specification.isAnnotationPresent( OnIterableElements.class ) )
        {
            checkArgument( Iterable.class.isAssignableFrom( propertyType ),
                "'propertyType' must be a subtype of Iterable." );

            final Class<?> elementType = specification.getAnnotation( OnIterableElements.class ).elementType( );

            final Equivalence<?> equivalenceOnElementType =
                    basicTypeEquivalenceComputer.computeEquivalenceOnBasicType(
                        equivalenceAnnotation, elementType )
                        .getEquivalence( );

            @SuppressWarnings( "unchecked" )
            final Class<? extends Iterable<?>> propertyTypeAsIterable = (Class<? extends Iterable<?>>) propertyType;

            return liftToIterable( equivalenceOnElementType, propertyTypeAsIterable );
        }

        return basicTypeEquivalenceComputer.computeEquivalenceOnBasicType( equivalenceAnnotation, propertyType );
    }

    private <X extends Iterable<?>> TypeEquivalence<X> liftToIterable( final Equivalence<?> equivalenceOnElementType,
            final Class<X> propertyType ) {

        @SuppressWarnings( "unchecked" )
        final Equivalence<X> equivalenceOnProperty =
                (Equivalence<X>) equivalenceFactory.createIterableEquivalence( equivalenceOnElementType, false );

        return new TypeEquivalence<X>( equivalenceOnProperty, propertyType );
    }

    private final EquivalenceFactory equivalenceFactory;
    private final AnnotationTypeReader annotationTypeReader;
    private final BasicTypeEquivalenceComputer basicTypeEquivalenceComputer;
}
