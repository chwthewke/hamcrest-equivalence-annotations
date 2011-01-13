package net.chwthewke.hamcrest.matchers.property;

import java.lang.reflect.Method;

public class PropertyFinder {
    public Method findProperty( final Class<?> clazz, final String propertyName,
            final Class<?> propertyType, final boolean allowNonPublic ) {
        final FindPropertyFunction finderFunction = allowNonPublic ?
                new FindVisiblePropertyFunction( ) : new FindPublicPropertyFunction( );
        return finderFunction.findPropertyMethod( clazz, propertyType, propertyName );
    }
}
