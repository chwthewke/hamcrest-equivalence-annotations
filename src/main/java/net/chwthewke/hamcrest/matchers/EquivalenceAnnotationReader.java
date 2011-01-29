package net.chwthewke.hamcrest.matchers;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;

class EquivalenceAnnotationReader {

    public EquivalenceAnnotationReader( final EquivalenceAnnotationInterpreters interpreters ) {
        this.interpreters = interpreters;
    }

    public <T> PropertyEquivalence<T, ?> createPropertyEquivalence( final Method specificationMethod,
            final Method property ) {

        final Class<? extends Annotation> equivalenceAnnotationType = getEquivalenceAnnotationType( specificationMethod );

        final Class<?> propertyType = Primitives.wrap( specificationMethod.getReturnType( ) );
        if ( equivalenceAnnotationType == ApproximateEquality.class )
            return createApproximateEquality( specificationMethod, property, propertyType );

        return createPropertyEquivalence( specificationMethod, equivalenceAnnotationType, property, propertyType );
    }

    private Class<? extends Annotation> getEquivalenceAnnotationType( final Method specificationMethod ) {
        final Collection<Class<? extends Annotation>> annotationsOnSpecification = Collections2.filter(
            exclusiveEquivalenceAnnotations,
            new Predicate<Class<? extends Annotation>>( ) {
                public boolean apply( final Class<? extends Annotation> input ) {
                    return specificationMethod.isAnnotationPresent( input );
                }
            } );

        final int annotationCount = annotationsOnSpecification.size( );
        if ( annotationCount == 0 )
            return Equality.class;
        if ( annotationCount == 1 )
            return Iterables.get( annotationsOnSpecification, 0 );
        throw new IllegalArgumentException(
            String.format(
                "The equivalence specification property %s has these mutually exclusive annotations: %s.",
                specificationMethod, newArrayList( annotationsOnSpecification ) ) );
    }

    private <T> PropertyEquivalence<T, Double> createApproximateEquality( final Method specificationMethod,
            final Method property, final Class<?> propertyType ) {
        if ( !Number.class.isAssignableFrom( propertyType ) )
            throw new IllegalArgumentException(
                String.format(
                    "The equivalence specification property %s bears %s, so it must have a type assignable to java.lang.Number.",
                    specificationMethod, ApproximateEquality.class.getSimpleName( ) ) );

        final Function<T, Double> propertyFunction =
                Functions.compose( toDouble, this.<T, Number>canonicalPropertyFunction( property, Number.class ) );

        final EquivalenceAnnotationInterpreter<Double> equivalenceAnnotationInterpreter =
                interpreters.approximateEqualityInterpreter( );

        return createPropertyEquivalence( specificationMethod,
            Double.class,
            propertyFunction,
            equivalenceAnnotationInterpreter );
    }

    private <T, U> PropertyEquivalence<T, U> createPropertyEquivalence( final Method specificationMethod,
            final Class<? extends Annotation> equivalenceAnnotationType,
            final Method property,
            final Class<U> propertyType ) {

        final EquivalenceAnnotationInterpreter<U> equivalenceAnnotationInterpreter =
                interpreters.selectAnnotationInterpreter( equivalenceAnnotationType, specificationMethod );

        return createPropertyEquivalence( specificationMethod,
            propertyType,
            this.<T, U>canonicalPropertyFunction( property, propertyType ),
            equivalenceAnnotationInterpreter );
    }

    private <T, U> PropertyEquivalence<T, U> createPropertyEquivalence( final Method specificationMethod,
            final Class<U> propertyType, final Function<T, U> propertyFunction,
            final EquivalenceAnnotationInterpreter<U> equivalenceAnnotationInterpreter ) {
        final String propertyName = specificationMethod.getName( );
        final Equivalence<U> equivalenceOnProperty =
                equivalenceAnnotationInterpreter.interpretAnnotation( specificationMethod, propertyType );
        return PropertyEquivalence.<T, U>create( propertyName, propertyFunction, equivalenceOnProperty );
    }

    private <T, U> Function<T, U> canonicalPropertyFunction( final Method property, final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( property, propertyType, item );
            }
        };
    }

    private <T, U> U extractProperty( final Method property, final Class<U> propertyType, final T item ) {

        final boolean wasAccessible = property.isAccessible( );
        try
        {
            property.setAccessible( true );
            final Object rawProperty = property.invoke( item );
            try
            {
                return propertyType.cast( rawProperty );
            }
            catch ( final ClassCastException e )
            {
                throw new RuntimeException(
                        String.format(
                            "Cannot cast result of property '%s()' on instance of %s to %s, actual type is %s.",
                                property.getName( ), item.getClass( ).getName( ),
                                propertyType.getName( ), rawProperty.getClass( ).getName( ) ),
                        e );
            }
        }
        catch ( final IllegalAccessException e )
        {
            throw new IllegalStateException( "Unpredicted illegal access", e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException(
                    String.format( "Exception while reading property %s on instance of %s.",
                            property.getName( ), item.getClass( ).getName( ) ),
                    e );
        }
        finally
        {
            property.setAccessible( wasAccessible );
        }
    }

    private final EquivalenceAnnotationInterpreters interpreters;

    private static final Function<Number, Double> toDouble =
            new Function<Number, Double>( ) {
                public Double apply( final Number input ) {
                    return input == null ? null : input.doubleValue( );
                }
            };

    @SuppressWarnings( "unchecked" )
    private static final Collection<Class<? extends Annotation>> exclusiveEquivalenceAnnotations =
            Lists.<Class<? extends Annotation>>newArrayList(
                Equality.class,
                Identity.class,
                ApproximateEquality.class,
                BySpecification.class,
                ByEquivalence.class );
}
