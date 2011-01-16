package net.chwthewke.hamcrest.matchers;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;

import java.lang.reflect.Method;

class FindVisiblePropertyFunction extends FindPropertyFunction {

    FindVisiblePropertyFunction( ) {
    }

    @Override
    protected Method getPropertyMethod( final Class<?> clazz, final String propertyName ) {
        try
        {
            final Method method = getAnyPropertyMethod( clazz, propertyName );

            if ( !isVisible( method.getDeclaringClass( ), clazz, method.getModifiers( ) ) )
                return raisePropertyNotFound( null, clazz, propertyName, VISIBLE_QUALIFIER );

            return method;
        }
        catch ( final NoSuchMethodException e )
        {
            return raisePropertyNotFound( e, clazz, propertyName, VISIBLE_QUALIFIER );
        }
    }

    private Method getAnyPropertyMethod( final Class<?> clazz, final String propertyName ) throws NoSuchMethodException {
        try
        {
            return clazz.getDeclaredMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            final Class<?> superClazz = clazz.getSuperclass( );
            if ( superClazz == null )
                throw e;
            return getAnyPropertyMethod( superClazz, propertyName );
        }
    }

    private boolean isVisible( final Class<?> declarationSite, final Class<?> callSite, final int modifiers ) {
        if ( isPublic( modifiers ) || isProtected( modifiers ) )
            return true;

        if ( isPrivate( modifiers ) )
            return declarationSite.equals( callSite );

        return declarationSite.getPackage( ).equals( callSite.getPackage( ) );
    }

    private static final String VISIBLE_QUALIFIER = "visible";
}
