package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.ByEquivalence;

final class ByEquivalenceInterpreter<U> implements EquivalenceAnnotationInterpreter<U> {
    public Equivalence<U> interpretAnnotation( final Method specificationMethod, final Class<U> propertyType ) {
        checkState( specificationMethod.isAnnotationPresent( ByEquivalence.class ) );

        final Class<? extends Equivalence<?>> equivalenceClass =
                specificationMethod.getAnnotation( ByEquivalence.class ).value( );

        checkEquivalenceType( specificationMethod, equivalenceClass, propertyType );

        // TODO Auto-generated method stub
        return null;
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
