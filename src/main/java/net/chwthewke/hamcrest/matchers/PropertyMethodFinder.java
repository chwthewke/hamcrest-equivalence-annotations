package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class PropertyMethodFinder {

    // TODO refactor to replace allowNonPublic with inheritance ?

    public Method findPropertyMethod( final Class<?> clazz,
            final Class<?> propertyType,
            final String propertyName,
            final boolean allowNonPublic ) {

        final Method property = allowNonPublic ?
                getVisiblePropertyMethod( clazz, propertyName ) :
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

    private Method getVisiblePropertyMethod( final Class<?> clazz, final String propertyName ) {
        Method method;
        try
        {
            method = getAnyPropertyMethod( clazz, propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            return raisePropertyNotFound( e, clazz, propertyName, VISIBLE_QUALIFIER );
        }

        final int mod = method.getModifiers( );

        if ( Modifier.isPublic( mod ) || Modifier.isProtected( mod ) )
            return method;

        if ( Modifier.isPrivate( mod ) )
        {
            if ( method.getDeclaringClass( ).equals( clazz ) )
                return method;
            return raisePropertyNotFound( null, clazz, propertyName, VISIBLE_QUALIFIER );
        }

        if ( !method.getDeclaringClass( ).getPackage( ).equals( clazz.getPackage( ) ) )
            return raisePropertyNotFound( null, clazz, propertyName, VISIBLE_QUALIFIER );

        return method;
    }

    private Method getAnyPropertyMethod( final Class<?> clazz, final String propertyName ) throws NoSuchMethodException {

        try
        {
            final Method property = clazz.getDeclaredMethod( propertyName );

            return property;
        }
        catch ( final NoSuchMethodException e )
        {
            final Class<?> superClazz = clazz.getSuperclass( );
            if ( superClazz == null )
                throw e;
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
