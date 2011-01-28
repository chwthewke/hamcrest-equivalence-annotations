package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.primitives.Primitives;

@Deprecated
class PropertyEquivalenceProvider<T> {

    public PropertyEquivalence<T, ?> get( ) {
        final Class<?> originalPropertyType = property.getReturnType( );
        final Class<?> propertyType = Primitives.wrap( originalPropertyType );

        if ( specificationMethod.isAnnotationPresent( BySpecification.class ) )
            return getBySpecificationTemplate( propertyType,
                    specificationMethod.getAnnotation( BySpecification.class ).value( ) );

        if ( specificationMethod.isAnnotationPresent( ByEquivalence.class ) )
            return getByEquivalenceTemplate( propertyType,
                    specificationMethod.getAnnotation( ByEquivalence.class ).value( ) );

        if ( specificationMethod.isAnnotationPresent( Equality.class ) )
            return getEqualityTemplate( propertyType );

        if ( specificationMethod.isAnnotationPresent( Identity.class ) )
        {
            if ( originalPropertyType.isPrimitive( ) )
                return getEqualityTemplate( propertyType );
            return getIdentityTemplate( propertyType );
        }

        if ( specificationMethod.isAnnotationPresent( ApproximateEquality.class ) )
            return getApproximateEqualityTemplate( propertyType,
                    specificationMethod.getAnnotation( ApproximateEquality.class ).tolerance( ) );

        return getEqualityTemplate( propertyType );
    }

    PropertyEquivalenceProvider(
            final PropertyFinder propertyFinder,
            final EquivalenceSpecificationValidator specificationValidator,
            final Method property,
            final Method specificationMethod ) {
        this.propertyFinder = propertyFinder;
        this.specificationValidator = specificationValidator;
        this.property = property;
        this.specificationMethod = specificationMethod;
    }

    private PropertyEquivalence<T, Double> getApproximateEqualityTemplate( final Class<?> propertyType,
                                                                           final double tolerance ) {
        checkState( propertyType == Double.class || propertyType == Float.class );

        final Function<Double, Matcher<? super Double>> closeToMatcherFactory =
                new Function<Double, Matcher<? super Double>>( ) {
                    public Matcher<? super Double> apply( final Double expected ) {
                        return Matchers.closeTo( expected, tolerance );
                    }
                };

        // This should be the safest way to extract the property to a Double.
        final Function<T, Double> propertyFunction =
                Functions.compose( new Function<Number, Double>( ) {
                    public Double apply( final Number number ) {
                        return number == null ? null : number.doubleValue( );
                    }
                }, propertyFunction( Number.class ) );

        return PropertyEquivalence.create(
                property.getName( ),
                propertyFunction,
                closeToMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getIdentityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return PropertyEquivalence.create(
                property.getName( ),
                propertyFunction( propertyType ),
                sameInstanceMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getEqualityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return PropertyEquivalence.create(
                property.getName( ),
                propertyFunction( propertyType ),
                equalToMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getByEquivalenceTemplate( final Class<U> propertyType,
            final Class<? extends Equivalence<?>> equivalenceClass ) {
        try
        {
            checkEquivalenceType( equivalenceClass, propertyType );

            final Constructor<? extends Equivalence<?>> ctor = equivalenceClass.getConstructor( );
            final Equivalence<?> equivalence = ctor.newInstance( );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new RuntimeException( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
        catch ( final InstantiationException e )
        {
            throw new RuntimeException( e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
        // TODO use reflection to check that 'equivalenceClass' is an equivalence on the correct type (U)

        return null;
    }

//    private Equivalence<?> createInstance( final Class<? extends Equivalence<?>> clazz ) {
//        final Constructor ctor;
//        try
//        {
//            ctor = clazz.getConstructor( );
//        }
//        catch ( final NoSuchMethodException e )
//        {
//            throw new IllegalArgumentException(
//                String.format( "Bad use of @%s: value %s must have a public no-args constructor",
//                    ByEquivalence.class.getSimpleName( ), clazz ) );
//        }
//        try
//        {
//            final Equivalence<?> instance = (Equivalence<?>) ctor.newInstance( );
//        }
//        catch ( final InstantiationException e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace( );
//        }
//        catch ( final IllegalAccessException e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace( );
//        }
//        catch ( final InvocationTargetException e )
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace( );
//        }
//    }

    private void checkEquivalenceType(
            final Class<? extends Equivalence<?>> equivalenceClass,
            final Class<?> propertyType ) {

        if ( Modifier.isAbstract( equivalenceClass.getModifiers( ) ) )
            throw new IllegalArgumentException( "" );
        // TODO

        try
        {
            final Class<?> equivalenceType = EQUIVALENCE_TYPE_FINDER.findExpectedType( equivalenceClass );
            if ( !equivalenceType.isAssignableFrom( propertyType ) )
                throw new IllegalArgumentException(
                        String.format(
                            "Bad use of @%s: value %s seems to implement %s<%s>, whereas property %s has type %s",
                            ByEquivalence.class.getSimpleName( ), equivalenceClass.getName( ),
                            Equivalence.class.getSimpleName( ), equivalenceType.getName( ),
                            property.getName( ), propertyType ) );
        }
        catch ( final IllegalArgumentException e )
        {
            throw new IllegalArgumentException(
                    String.format( "%s is not a valid implementation of %s: %s",
                        equivalenceClass.getName( ), Equivalence.class.getSimpleName( ), e.getMessage( ) ) );
        }

    }

    private <U> PropertyEquivalence<T, U> getBySpecificationTemplate( final Class<U> propertyType,
                                                                      final Class<?> propertySpecification ) {

        final CompositeMatcherFactory<U> matcherFactoryForProperty =
                new CompositeMatcherFactory<U>( propertyFinder,
                        specificationValidator,
                        new EquivalenceAnnotationReader( new EquivalenceAnnotationInterpreters( ) ),
                        propertyType, propertySpecification );

        final Function<U, Matcher<? super U>> matcherBySpecificationFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return matcherFactoryForProperty.equivalentTo( expected );
                    }
                };
        return PropertyEquivalence.create(
                property.getName( ),
                propertyFunction( propertyType ),
                matcherBySpecificationFactory );
    }

    private <U> Function<T, U> propertyFunction( final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( propertyType, item );
            }
        };
    }

    private <U> U extractProperty( final Class<U> propertyType, final T item ) {

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

    private final PropertyFinder propertyFinder;
    private final EquivalenceSpecificationValidator specificationValidator;

    private final Method property;
    private final Method specificationMethod;

    private static final ReflectiveTypeFinder EQUIVALENCE_TYPE_FINDER = new ReflectiveTypeFinder( "equivalentTo", 1, 0 );
}
