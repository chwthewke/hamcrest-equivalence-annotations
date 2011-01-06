package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

class PropertyMethodFinder {

    public Method findPropertyMethod( final Class<?> clazz,
            final Class<?> propertyType,
            final String propertyName,
            final boolean allowNonPublic ) {

        final Method property;
        try
        {
            property = clazz.getMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new IllegalArgumentException(
                String.format( "The matched class %s lacks the property method '%s()'.",
                    clazz.getName( ), propertyName ), e );
        }

        if ( !propertyType.isAssignableFrom( property.getReturnType( ) ) )
            throw new IllegalArgumentException(
                String.format(
                    "The property '%s()' on %s has return type %s which is not assignable to %s.",
                    propertyName,
                    clazz.getName( ),
                    property.getReturnType( ).getName( ),
                    propertyType.getName( ) ) );

        return property;
    }
}
