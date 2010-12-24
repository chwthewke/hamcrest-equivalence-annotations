package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

class ExpectedPropertyTemplateFactory<T> {

    public ExpectedPropertyTemplate<T, ?> getExpectedPropertyTemplate( ) {
        final Class<?> originalPropertyType = property.getReturnType( );
        final Class<?> propertyType = toReference( originalPropertyType );

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
                specificationMethod.getAnnotation( ApproximateEquality.class ).value( ) );

        return getEqualityTemplate( propertyType );
    }

    ExpectedPropertyTemplateFactory( final Method property, final Method specificationMethod ) {
        this.property = property;
        this.specificationMethod = specificationMethod;
    }

    private ExpectedPropertyTemplate<T, Double> getApproximateEqualityTemplate( final Class<?> propertyType,
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

        return ExpectedPropertyTemplate.<T, Double>create(
            property.getName( ),
            propertyFunction,
            closeToMatcherFactory );
    }

    private <U> ExpectedPropertyTemplate<T, U> getIdentityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return ExpectedPropertyTemplate.<T, U>create(
            property.getName( ),
            propertyFunction( propertyType ),
            sameInstanceMatcherFactory );
    }

    private <U> ExpectedPropertyTemplate<T, U> getEqualityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return ExpectedPropertyTemplate.<T, U>create(
            property.getName( ),
            propertyFunction( propertyType ),
            equalToMatcherFactory );
    }

    private <U> Function<T, U> propertyFunction( final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( propertyType, item );
            }
        };
    }

    private <U> U extractProperty( final Class<U> propertyType, final T item ) {
        try
        {
            final Object rawProperty = property.invoke( item );
            try
            {
                return propertyType.cast( rawProperty );
            }
            catch ( final ClassCastException e )
            {
                throw new RuntimeException(
                    String.format( "Cannot cast result of property '%s()' on instance of %s to %s, actual type is %s.",
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
    }

    @SuppressWarnings( "unchecked" )
    private static <T> Class<T> toReference( final Class<T> type ) {
        if ( !type.isPrimitive( ) )
            return type;
        return (Class<T>) primitiveToReference.get( type );
    }

    private final Method property;
    private final Method specificationMethod;

    private static final Map<Class<?>, Class<?>> primitiveToReference =
            ImmutableMap.<Class<?>, Class<?>>builder( )
                .put( Boolean.TYPE, Boolean.class )
                .put( Byte.TYPE, Byte.class )
                .put( Character.TYPE, Character.class )
                .put( Short.TYPE, Short.class )
                .put( Integer.TYPE, Integer.class )
                .put( Long.TYPE, Long.class )
                .put( Float.TYPE, Float.class )
                .put( Double.TYPE, Double.class )
                .build( );

}
