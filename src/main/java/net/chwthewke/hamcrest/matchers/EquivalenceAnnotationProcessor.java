package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Functions.compose;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.primitives.Primitives.wrap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.equivalence.ApproximateEqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IdentityEquivalence;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

final class EquivalenceAnnotationProcessor<T, V> {

    public static <T> EquivalenceAnnotationProcessor<T, ?> annotationProcessorFor(
                    final Class<T> sourceType,
                    final Method specification,
                    final Method target ) {
        return annotationProcessorFor( sourceType, wrap( specification.getReturnType( ) ), specification, target );
    }

    private static <T, V> EquivalenceAnnotationProcessor<T, V> annotationProcessorFor(
                    @SuppressWarnings( "unused" ) final Class<T> sourceType,
                    final Class<V> propertyType,
                    final Method specification,
                    final Method target ) {
        return new EquivalenceAnnotationProcessor<T, V>( propertyType, specification, target );
    }

    private EquivalenceAnnotationProcessor( final Class<V> propertyType, final Method specification, final Method target ) {
        this.propertyType = propertyType;
        this.specification = specification;
        this.target = target;
    }

    public Equivalence<T> processEquivalenceSpecification( ) {

        annotationType = new AnnotationTypeReader( ).getEquivalenceAnnotationType( specification );

        if ( annotationType == ApproximateEquality.class )
        {
            checkArgument(
                Number.class.isAssignableFrom( propertyType ),
                String.format(
                    "The equivalence specification property %s bears %s, so it must have a type assignable to java.lang.Number.",
                    specification, ApproximateEquality.class.getSimpleName( ) ) );

            final Lift<Double> lift = lift( Double.class,
                compose( toDouble, new ReadPropertyFunction<T, Number>( target, Number.class ) ) );

            final double tolerance = getSpecificationAnnotation( ApproximateEquality.class ).tolerance( );

            return lift.computeEquivalence( new ApproximateEqualityEquivalence( tolerance ) );
        }

        return computeGenericEquivalence( );
    }

    private Equivalence<T> computeGenericEquivalence( ) {
        final Lift<V> lift = lift( propertyType, new ReadPropertyFunction<T, V>( target, propertyType ) );

        final Equivalence<V> propertyEquivalence;

        if ( annotationType == ByEquivalence.class )
        {
            final Class<? extends Equivalence<?>> equivalenceClass = getSpecificationAnnotation( ByEquivalence.class ).value( );
            checkEquivalenceType( specification, equivalenceClass );
            propertyEquivalence = createInstance( equivalenceClass );
        }
        else if ( annotationType == BySpecification.class )
        {
            // TODO validation
            propertyEquivalence = new CompositeMatcherFactory<V>(
                new PropertyFinder( ),
                new EquivalenceSpecificationValidator( ),
                propertyType,
                getSpecificationAnnotation( BySpecification.class ).value( ) );
        }
        else if ( annotationType == Identity.class && !specification.getReturnType( ).isPrimitive( ) )
        {
            propertyEquivalence = new IdentityEquivalence<V>( );
        }
        else if ( annotationType == Identity.class || annotationType == Equality.class )
        {
            propertyEquivalence = new EqualityEquivalence<V>( );
        }
        else
        {
            throw new IllegalStateException( String.format( "Cannot process annotation %s of type %s.",
                getSpecificationAnnotation( annotationType ), annotationType.getSimpleName( ) ) );
        }

        return lift.computeEquivalence( propertyEquivalence );
    }

    private <A extends Annotation> A getSpecificationAnnotation( final Class<A> annotationClass ) {
        checkState( specification.isAnnotationPresent( annotationClass ) );
        return specification.getAnnotation( annotationClass );
    }

    private void checkEquivalenceType(
            final Method specificationMethod,
            final Class<? extends Equivalence<?>> equivalenceClass ) {

        final int mods = equivalenceClass.getModifiers( );
        if ( Modifier.isAbstract( mods ) )
            throw new IllegalArgumentException(
                formatMisuse( "value %s cannot be an abstract class.", equivalenceClass ) );
        if ( !Modifier.isPublic( mods ) )
            throw new IllegalArgumentException(
                formatMisuse( "value %s must be a public class.", equivalenceClass ) );

        try
        {
            final Class<?> equivalenceType = equivalenceTypeFinder.findExpectedType( equivalenceClass );
            if ( !equivalenceType.isAssignableFrom( propertyType ) )
                throw new IllegalArgumentException(
                        formatMisuse(
                            "value %s seems to implement %s<%s>, whereas property %s has type %s",
                            ByEquivalence.class.getSimpleName( ), equivalenceClass.getName( ),
                            Equivalence.class.getSimpleName( ), equivalenceType.getName( ),
                            specificationMethod.getName( ), propertyType ) );
        }
        catch ( final IllegalArgumentException e )
        {
            throw new IllegalArgumentException(
                    formatMisuse( "%s is not a valid implementation of %s: %s",
                        equivalenceClass.getName( ), Equivalence.class.getSimpleName( ), e.getMessage( ) ) );
        }

    }

    @SuppressWarnings( "unchecked" )
    private Equivalence<V> createInstance( final Class<? extends Equivalence<?>> equivalenceClass ) {

        final Constructor<? extends Equivalence<?>> ctor = getDefaultConstructor( equivalenceClass );

        try
        {
            return (Equivalence<V>) ctor.newInstance( );
        }
        catch ( final IllegalArgumentException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final InstantiationException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final IllegalAccessException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException(
                String.format( "Exception while calling the default constructor of %s.", equivalenceClass ),
                e );
        }
    }

    private RuntimeException onReflectiveException( final Class<? extends Equivalence<?>> equivalenceClass,
            final Exception reflectiveException ) {

        final String message = String.format(
            "Unexpected reflective exception while calling the default constructor of %s.",
            equivalenceClass );

        return new RuntimeException( message, reflectiveException );
    }

    private String formatMisuse( final String format, final Object... arguments ) {
        return String.format( "Bad use of @%s: %s", ByEquivalence.class, String.format( format, arguments ) );
    }

    private <X> Constructor<X> getDefaultConstructor( final Class<X> clazz ) {
        try
        {
            return clazz.getConstructor( );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new IllegalArgumentException(
                formatMisuse( "%s must have a public no-arg constructor.", clazz ) );
        }
    }

    private final Method specification;
    private final Method target;

    private final Class<V> propertyType;
    private Class<? extends Annotation> annotationType;

    private static final Function<Number, Double> toDouble =
            new Function<Number, Double>( ) {
                public Double apply( final Number input ) {
                    return input == null ? null : input.doubleValue( );
                }
            };

    private static final ReflectiveTypeFinder equivalenceTypeFinder =
            new ReflectiveTypeFinder( "equivalentTo", 1, 0 );

    private <U> Lift<U> lift( @SuppressWarnings( "unused" ) final Class<U> propType,
            final Function<T, U> propertyFunction ) {
        return new Lift<U>( propertyFunction );
    }

    private class Lift<U> {

        public Lift( final Function<T, U> propertyFunction ) {
            this.propertyFunction = propertyFunction;
        }

        public Equivalence<T> computeEquivalence( final Equivalence<? super U> equivalence ) {
            return LiftedEquivalence.create( specification.getName( ), propertyFunction, equivalence );
        }

        private final Function<T, U> propertyFunction;
    }

    private static class AnnotationTypeReader {
        public Class<? extends Annotation> getEquivalenceAnnotationType( final Method specificationMethod ) {
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

        @SuppressWarnings( "unchecked" )
        private static final Collection<Class<? extends Annotation>> exclusiveEquivalenceAnnotations =
                Lists.<Class<? extends Annotation>>newArrayList(
                    Equality.class,
                    Identity.class,
                    ApproximateEquality.class,
                    BySpecification.class,
                    ByEquivalence.class );

    }
}
