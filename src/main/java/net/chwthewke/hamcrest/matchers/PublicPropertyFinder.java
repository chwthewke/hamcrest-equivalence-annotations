package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

class PublicPropertyFinder extends PropertyFinder {

    public PublicPropertyFinder( ) {
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
