package net.chwthewke.hamcrest.matchers.property;

import java.lang.reflect.Method;

class FindPublicPropertyFunction extends FindPropertyFunction {

    protected FindPublicPropertyFunction( ) {
    }

    @Override
    protected Method getPropertyMethod( final Class<?> clazz, final String propertyName ) {
        try
        {
            return clazz.getMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            return raisePropertyNotFound( e, clazz, propertyName, PUBLIC_QUALIFIER );
        }
    }

    private static final String PUBLIC_QUALIFIER = "public";
}
