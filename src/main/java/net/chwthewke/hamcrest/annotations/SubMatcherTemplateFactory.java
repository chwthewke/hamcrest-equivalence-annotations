package net.chwthewke.hamcrest.annotations;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

class SubMatcherTemplateFactory<T> {

    public SubMatcherTemplate<T, ?> getSubMatcherTemplate( ) {
        final Class<?> propertyType = toReference( property.getReturnType( ) );

        if ( specificationMethod.isAnnotationPresent( Equality.class ) )
            return getEqualitySubMatcherTemplate( propertyType );
        if ( specificationMethod.isAnnotationPresent( Identity.class ) )
            return getIdentitySubMatcherTemplate( propertyType );
        if ( specificationMethod.isAnnotationPresent( ApproximateEquality.class ) )
            return getApproximateEqualitySubMatcherTemplate( propertyType,
                specificationMethod.getAnnotation( ApproximateEquality.class ).value( ) );
        // TODO sensible defaults
        throw new UnsupportedOperationException( );
    }

    SubMatcherTemplateFactory( final Method property, final Method specificationMethod ) {
        this.property = property;
        this.specificationMethod = specificationMethod;
    }

    private SubMatcherTemplate<T, Double> getApproximateEqualitySubMatcherTemplate( final Class<?> propertyType,
            final double tolerance ) {
        // TODO allow Float as well.
        checkState( propertyType == Double.class );

        final Function<Double, Matcher<? super Double>> closeToMatcherFactory =
                new Function<Double, Matcher<? super Double>>( ) {
                    public Matcher<? super Double> apply( final Double expected ) {
                        return Matchers.closeTo( expected, tolerance );
                    }
                };

        return SubMatcherTemplate.<T, Double>create(
            property.getName( ),
            propertyFunction( Double.class ),
            closeToMatcherFactory );
    }

    private <U> SubMatcherTemplate<T, U> getIdentitySubMatcherTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return SubMatcherTemplate.<T, U>create(
            property.getName( ),
            propertyFunction( propertyType ),
            sameInstanceMatcherFactory );
    }

    private <U> SubMatcherTemplate<T, U> getEqualitySubMatcherTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return SubMatcherTemplate.<T, U>create(
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
