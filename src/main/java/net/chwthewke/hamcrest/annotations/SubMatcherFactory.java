package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public class SubMatcherFactory<T> {

    public SubMatcherFactory( final Class<T> matchedClass ) {
        this.matchedClass = matchedClass;
    }

    public SubMatcher<T> createSubMatcher( final Method propertyDescriptor, final T expected ) {
        try
        {
            final Method propertyMethod = matchedClass.getMethod( propertyDescriptor.getName( ) );

            if ( !propertyDescriptor.getReturnType( ).isAssignableFrom( propertyMethod.getReturnType( ) ) )
                throw new IllegalArgumentException(
                    String.format( "Incompatible return types: %s: %s vs. %s",
                        propertyDescriptor.getName( ), propertyDescriptor.getReturnType( ),
                        propertyMethod.getReturnType( ) ) );

            // TODO expected property binding time
            final Object expectedProperty = propertyMethod.invoke( expected );

            // TODO better default than @Equals
            Matcher<?> propertyMatcher;
            if ( propertyDescriptor.isAnnotationPresent( Identity.class ) )
                propertyMatcher = sameInstance( expectedProperty );
            else if ( propertyDescriptor.isAnnotationPresent( ApproximateEquality.class ) )
            {
                // TODO type check
                propertyMatcher = closeTo( (Double) expectedProperty,
                    propertyDescriptor.getAnnotation( ApproximateEquality.class ).value( ) );
            }
            else
                propertyMatcher = equalTo( expectedProperty );

            return createSubMatcher( propertyMethod, propertyMatcher );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new IllegalArgumentException(
                String.format( "Missing method %s() on type %s", propertyDescriptor.getName( ), matchedClass ),
                e );
        }
        catch ( final IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
    }

    public SubMatcher<T> createSubMatcher( final Method extractor, final Matcher<?> matcher ) {

        // TODO duplication of Method.invoke + rethrow as unchecked
        final String propertyName = extractor.getName( );
        final Function<T, ?> extractorFunction = new Function<T, Object>( ) {
            public Object apply( final T item ) {
                try
                {
                    return extractor.invoke( item );
                }
                catch ( final IllegalAccessException e )
                {
                    // TODO Auto-generated catch block
                    throw new RuntimeException( e );
                }
                catch ( final InvocationTargetException e )
                {
                    // TODO Auto-generated catch block
                    throw new RuntimeException( e );
                }
            }
        };

        return createSubMatcher( propertyName, extractorFunction, matcher );
    }

    public <U> SubMatcher<T> createSubMatcher( final String propertyName,
            final Function<T, ?> propertyExtractor,
            final Matcher<?> propertyMatcher ) {
        return new SubMatcher<T>( propertyName, propertyExtractor, propertyMatcher );
    }

    private final Class<T> matchedClass;
}
