package net.chwthewke.hamcrest.matchers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

class EquivalenceSpecificationValidator {

    public void validateSpecificationInterface( final Class<?> equivalenceSpecification ) {

        checkSpecificationIsAPublicInterface( equivalenceSpecification );

        checkMatcherOfAnnotation( equivalenceSpecification );

        checkSpecificationIsNotEmpty( equivalenceSpecification );

        for ( final Method method : equivalenceSpecification.getMethods( ) )
            checkValidSpecificationProperty( method );
    }

    private void checkSpecificationIsAPublicInterface( final Class<?> equivalenceSpecification ) {
        if ( !equivalenceSpecification.isInterface( ) )
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must be an interface.",
                    equivalenceSpecification.getName( ) ) );

        if ( !Modifier.isPublic( equivalenceSpecification.getModifiers( ) ) )
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must have public visibility.",
                    equivalenceSpecification.getName( ) ) );
    }

    private static void checkMatcherOfAnnotation( final Class<?> equivalenceSpecification ) {
        if ( !equivalenceSpecification.isAnnotationPresent( EquivalenceSpecificationOn.class ) )
        {
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must be annotated with %s.",
                    equivalenceSpecification.getName( ),
                    EquivalenceSpecificationOn.class.getSimpleName( ) ) );
        }
    }

    private void checkSpecificationIsNotEmpty( final Class<?> equivalenceSpecification ) {
        if ( equivalenceSpecification.getMethods( ).length == 0 )
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must have at least one method.",
                    equivalenceSpecification.getName( ) ) );
    }

    private void checkValidSpecificationProperty( final Method method ) {
        if ( method.getReturnType( ) == void.class )
            throw new IllegalArgumentException(
                String.format( "The method %s in specification %s has return type void.",
                    method,
                    method.getDeclaringClass( ).getName( ) ) );

        if ( method.getParameterTypes( ).length != 0 )
            throw new IllegalArgumentException(
                String.format( "The method %s in specification %s has parameters.",
                    method,
                    method.getDeclaringClass( ).getName( ) ) );
    }

}
