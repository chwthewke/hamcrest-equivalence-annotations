package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.base.Function;

public abstract class SubMatcherTemplateFactory<T, U> {

    public abstract SubMatcherTemplate<T, U> getSubMatcherTemplate( );

    protected SubMatcherTemplateFactory( final Method property, final String propertyName ) {
        this.property = property;
        this.propertyName = propertyName;
    }

    // TODO something can, and will go wrong if propertyType is property.getReturnType() and primitive.
    protected Function<T, U> propertyFunction( final Method property, final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( propertyType, property, item );
            }
        };
    }

    protected U extractProperty( final Class<U> propertyType,
            final Method property, final T item ) {

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

    protected final Method property;
    protected final String propertyName;

}
