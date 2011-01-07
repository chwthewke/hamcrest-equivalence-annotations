package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

class PropertyMethodFinder {

    public Method findPropertyMethod( final Class<?> clazz,
            final Class<?> propertyType,
            final String propertyName,
            final boolean allowNonPublic ) {

        final Method property = allowNonPublic ?
                getAnyPropertyMethod( clazz, propertyName ) :
                getPublicPropertyMethod( clazz, propertyName );

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

    private Method getAnyPropertyMethod( final Class<?> clazz, final String propertyName ) {

        try
        {
            final Method property = clazz.getDeclaredMethod( propertyName );

            return property;
        }
        catch ( final NoSuchMethodException e )
        {
            final Class<?> superClazz = clazz.getSuperclass( );
            if ( superClazz == null )
                raisePropertyNotFound( e, clazz, VISIBLE_QUALIFIER, propertyName );
            return getAnyPropertyMethod( superClazz, propertyName );
        }
    }

    private Method getPublicPropertyMethod( final Class<?> clazz, final String propertyName ) {
        final Method property;
        try
        {
            property = clazz.getMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            return raisePropertyNotFound( e, clazz, propertyName, PUBLIC_QUALIFIER );
        }
        return property;
    }

    private Method raisePropertyNotFound( final NoSuchMethodException e, final Class<?> clazz,
            final String propertyName,
            final String qualifier ) {
        throw new IllegalArgumentException(
            String.format( "The matched class %s lacks the %s property '%s()'.",
                clazz.getName( ), qualifier, propertyName ), e );
    }

    private static final String PUBLIC_QUALIFIER = "public";
    private static final String VISIBLE_QUALIFIER = "visible";
}
