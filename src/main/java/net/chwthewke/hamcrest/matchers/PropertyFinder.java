package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

class PropertyFinder {
    public Method findProperty( final Class<?> clazz, final Class<?> propertyType,
            final String propertyName, final boolean allowNonPublic ) {
        final FindPropertyFunction finderFunction = allowNonPublic ?
                new FindVisiblePropertyFunction( ) : new FindPublicPropertyFunction( );
        return finderFunction.findPropertyMethod( clazz, propertyType, propertyName );
    }
}
