package net.chwthewke.hamcrest.matchers.specification;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.MatcherOf;

public class MatcherSpecificationValidator {

    public void validateSpecificationInterface( final Class<?> matcherSpecification ) {

        checkSpecificationIsAPublicInterface( matcherSpecification );

        checkMatcherOfAnnotation( matcherSpecification );

        for ( final Method method : matcherSpecification.getMethods( ) )
            checkValidSpecificationProperty( method );
    }

    private void checkSpecificationIsAPublicInterface( final Class<?> matcherSpecification ) {
        if ( !matcherSpecification.isInterface( ) )
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must be an interface.",
                    matcherSpecification.getName( ) ) );

        if ( !Modifier.isPublic( matcherSpecification.getModifiers( ) ) )
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must have public visibility.",
                    matcherSpecification.getName( ) ) );
    }

    private static void checkMatcherOfAnnotation( final Class<?> matcherSpecification ) {
        if ( !matcherSpecification.isAnnotationPresent( MatcherOf.class ) )
        {
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must be annotated with %s.",
                    matcherSpecification.getName( ),
                    MatcherOf.class.getSimpleName( ) ) );
        }
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
