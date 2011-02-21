package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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

    // TODO rename, remove 'property' ?
    public TypeEquivalence<?> computeEquivalenceOnPropertyType( final Method specification ) {
        final Class<?> propertyType = specification.getReturnType( );
        final Annotation equivalenceAnnotation =
                checkNotNull( annotationTypeReader.getEquivalenceAnnotation( specification ),
                    "Unexpected missing annotation." );
        return basicTypeEquivalenceComputer.computeEquivalenceOnBasicPropertyType( equivalenceAnnotation, propertyType );
    }

//    private TypeEquivalence<?> computeIterableEquivalenceOnPropertyType(
//            final Method specification ) {
//
//        checkState( Iterable.class.isAssignableFrom( propertyType ),
//            "'propertyType' must be a subtype of Iterable." );
//
//        final Equivalence<?> equivalenceOnElementType =
//                basicTypeEquivalenceComputer.computeEquivalenceOnBasicPropertyType( )
//                    .getEquivalence( );
//
//        return liftToIterable( equivalenceOnElementType, (Class<? extends Iterable<?>>) propertyType );
//    }

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
