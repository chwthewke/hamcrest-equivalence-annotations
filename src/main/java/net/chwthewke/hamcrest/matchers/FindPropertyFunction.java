package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;

abstract class FindPropertyFunction {

    FindPropertyFunction( ) {
    }

    protected abstract Method getPropertyMethod( final Class<?> clazz, final String propertyName );

    public Method findPropertyMethod( final Class<?> clazz, final Class<?> propertyType, final String propertyName ) {
        final Method property = getPropertyMethod( clazz, propertyName );

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

    IllegalArgumentException propertyNotFoundException( final NoSuchMethodException e, final Class<?> clazz,
            final String propertyName, final String qualifier ) {
        return new IllegalArgumentException(
            String.format( "The matched class %s lacks the %s property '%s()'.",
                clazz.getName( ), qualifier, propertyName ), e );
    }

}
