package net.chwthewke.hamcrest.annotations;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

public class AnnotationMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public static <T> AnnotationMatcher<T> of( final Class<T> matchedClass, final Class<?> matcherSpecification,
            final T expected ) {
        return new AnnotationMatcher<T>( expected, matchedClass, matcherSpecification );
    }

    private AnnotationMatcher( final T expected,
            final Class<T> matchedClass,
            final Class<?> matcherSpecification ) {
        super( matchedClass );
        this.expected = expected;
        this.matcherSpecification = matcherSpecification;
        this.matchedClass = matchedClass;

        checkMatchedClass( );
        try
        {
            initSubMatchers( );
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
        catch ( final SecurityException e )
        {
            // TODO Auto-generated catch block
            throw new RuntimeException( e );
        }
        catch ( final NoSuchMethodException e )
        {
            // TODO Auto-generated catch block
            throw new RuntimeException( e );
        }
    }

    public void describeTo( final Description description ) {

        description
            .appendText( "a " )
            .appendText( matchedClass.getSimpleName( ) );

        String subMatcherLeadin = " with";
        for ( final SubMatcher<T> subMatcher : subMatchers )
        {
            description
                .appendText( subMatcherLeadin )
                .appendText( " " )
                .appendDescriptionOf( subMatcher );
            subMatcherLeadin = ",";
        }
    }

    private void checkMatchedClass( ) {
        final MatcherOf annotation =
                matcherSpecification.getAnnotation( MatcherOf.class );
        if ( annotation == null )
            throw new IllegalArgumentException( /* TODO */);
        if ( annotation.value( ) != matchedClass ) // TODO can use isAssignableFrom ?
            throw new IllegalArgumentException( /* TODO */);
    }

    @Override
    protected boolean matchesSafely( final T item, final Description mismatchDescription ) {
        for ( final SubMatcher<T> subMatcher : subMatchers )
        {
            if ( !subMatcher.matches( item ) )
            {
                subMatcher.describeMismatch( item, mismatchDescription );
                return false;
            }
        }

        return true;
    }

    private void initSubMatchers( ) throws IllegalAccessException, InvocationTargetException, SecurityException,
            NoSuchMethodException {
        final List<Method> methods = newArrayList( matcherSpecification.getMethods( ) );

        Collections.sort( methods,
            Ordering.natural( ).onResultOf( new Function<Method, String>( ) {
                public String apply( final Method method ) {
                    return method.getName( );
                }
            } ) );

        final SubMatcherFactory<T> factory = new SubMatcherFactory<T>( matchedClass );

        for ( final Method propertyDescriptor : methods )
        {

            final SubMatcher<T> sub = factory.createSubMatcher( propertyDescriptor, expected );
            subMatchers.add( sub );
        }
    }

    private final T expected;
    private final Class<?> matcherSpecification;
    private final Class<T> matchedClass;

    private final Collection<SubMatcher<T>> subMatchers =
            new ArrayList<SubMatcher<T>>( );

}
