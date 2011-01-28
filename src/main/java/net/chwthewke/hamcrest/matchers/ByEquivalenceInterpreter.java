package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.ByEquivalence;

final class ByEquivalenceInterpreter<U> implements EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( ByEquivalence.class ) );

        final Class<? extends Equivalence<?>> equivalenceClass =
                specificationMethod.getAnnotation( ByEquivalence.class ).value( );

        checkEquivalenceType( specificationMethod, equivalenceClass, propertyType );

        return createInstance( equivalenceClass );
    }

    @SuppressWarnings( "unchecked" )
    private Equivalence<U> createInstance( final Class<? extends Equivalence<?>> equivalenceClass ) {

        final Constructor<? extends Equivalence<?>> ctor = getDefaultConstructor( equivalenceClass );

        try
        {
            return (Equivalence<U>) ctor.newInstance( );
        }
        catch ( final IllegalArgumentException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final InstantiationException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final IllegalAccessException e )
        {
            throw onReflectiveException( equivalenceClass, e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException(
                String.format( "Exception while calling the default constructor of %s.", equivalenceClass ),
                e );
        }
    }

    private RuntimeException onReflectiveException( final Class<? extends Equivalence<?>> equivalenceClass,
            final Exception reflectiveException ) {

        final String message = String.format(
            "Unexpected reflective exception while calling the default constructor of %s.",
            equivalenceClass );

        return new RuntimeException( message, reflectiveException );
    }

    private <X> Constructor<X> getDefaultConstructor( final Class<X> clazz ) {
        try
        {
            return clazz.getConstructor( );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new IllegalArgumentException(
                formatMisuse( "%s must have a public no-arg constructor.", clazz ) );
        }
    }

    private void checkEquivalenceType(
            final Method specificationMethod,
            final Class<? extends Equivalence<?>> equivalenceClass,
            final Class<?> propertyType ) {

        final int mods = equivalenceClass.getModifiers( );
        if ( Modifier.isAbstract( mods ) )
            throw new IllegalArgumentException(
                formatMisuse( "value %s cannot be an abstract class.", equivalenceClass ) );
        if ( !Modifier.isPublic( mods ) )
            throw new IllegalArgumentException(
                formatMisuse( "value %s must be a public class.", equivalenceClass ) );

        try
        {
            final Class<?> equivalenceType = equivalenceTypeFinder.findExpectedType( equivalenceClass );
            if ( !equivalenceType.isAssignableFrom( propertyType ) )
                throw new IllegalArgumentException(
                        formatMisuse(
                            "value %s seems to implement %s<%s>, whereas property %s has type %s",
                            ByEquivalence.class.getSimpleName( ), equivalenceClass.getName( ),
                            Equivalence.class.getSimpleName( ), equivalenceType.getName( ),
                            specificationMethod.getName( ), propertyType ) );
        }
        catch ( final IllegalArgumentException e )
        {
            throw new IllegalArgumentException(
                    formatMisuse( "%s is not a valid implementation of %s: %s",
                        equivalenceClass.getName( ), Equivalence.class.getSimpleName( ), e.getMessage( ) ) );
        }

    }

    private String formatMisuse( final String format, final Object... arguments ) {
        return String.format( "Bad use of @%s: %s", ByEquivalence.class, String.format( format, arguments ) );
    }

    private static final ReflectiveTypeFinder equivalenceTypeFinder =
            new ReflectiveTypeFinder( "equivalentTo", 1, 0 );

}
