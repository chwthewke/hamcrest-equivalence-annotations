package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;

final class ReadPropertyFunction<T, U> implements Function<T, U> {

    ReadPropertyFunction( final Method property, final Class<U> propertyType ) {
        this.property = property;
        this.propertyType = propertyType;
    }

    public U apply( final T item ) {
        final boolean wasAccessible = property.isAccessible( );
        try
        {
            property.setAccessible( true );
            return invokeAndCast( item );
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

    private U invokeAndCast( final T item ) throws IllegalAccessException, InvocationTargetException {
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

    @VisibleForTesting
    Method getProperty( ) {
        return property;
    }

    private final Method property;
    private final Class<U> propertyType;
}
