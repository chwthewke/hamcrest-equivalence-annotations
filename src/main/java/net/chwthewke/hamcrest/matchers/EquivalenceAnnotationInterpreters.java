package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

import org.hamcrest.Matcher;

public class EquivalenceAnnotationInterpreters {

    private final class ByEquivalenceInterpreter<U> implements
            EquivalenceAnnotationInterpreter<U> {
        public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private final class BySpecificationInterpreter<U> implements
            EquivalenceAnnotationInterpreter<U> {
        public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
            checkState( specificationMethod.isAnnotationPresent( BySpecification.class ) );

            final BySpecification annotation = specificationMethod.getAnnotation( BySpecification.class );

            // TODO redo injection
            return new CompositeMatcherFactory<U>(
                        new PropertyFinder( ),
                        new EquivalenceSpecificationValidator( ),
                        new EquivalenceAnnotationReader( new EquivalenceAnnotationInterpreters( ) ),
                        propertyType,
                        annotation.value( ) );
        }
    }

    private final class IdentityInterpreter<U> implements
            EquivalenceAnnotationInterpreter<U> {
        public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
            checkState( specificationMethod.isAnnotationPresent( Identity.class ) );

            return new Equivalence<U>( ) {
                public Matcher<U> equivalentTo( final U expected ) {
                    return sameInstance( expected );
                }
            };
        }
    }

    private final class EqualityInterpreter<U> implements
            EquivalenceAnnotationInterpreter<U> {
        public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
            return new Equivalence<U>( ) {
                public Matcher<U> equivalentTo( final U expected ) {
                    return equalTo( expected );
                }
            };
        }
    }

    private final class ApproximateEqualityInterpreter implements
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

    public EquivalenceAnnotationInterpreter<Double> approximateEqualityInterpreter( ) {
        return new ApproximateEqualityInterpreter( );
    }

    public <U> EquivalenceAnnotationInterpreter<U> selectAnnotationInterpreter(
            final Class<? extends Annotation> equivalenceAnnotationType, final Method specificationMethod ) {

        if ( equivalenceAnnotationType == Equality.class )
        {
            return new EqualityInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == Identity.class )
        {
            return specificationMethod.getReturnType( ).isPrimitive( )
                    ? new EqualityInterpreter<U>( )
                    : new IdentityInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == BySpecification.class )
        {
            return new BySpecificationInterpreter<U>( );
        }

        if ( equivalenceAnnotationType == ByEquivalence.class )
        {
            return new ByEquivalenceInterpreter<U>( );
        }

        throw new IllegalArgumentException(
            String.format( "Unknown equivalence annotation %s", equivalenceAnnotationType.getName( ) ) );
    }
}
