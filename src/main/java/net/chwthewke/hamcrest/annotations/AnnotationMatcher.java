package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class AnnotationMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public static <T> AnnotationMatcher<T> of( final Class<?> matcherSpecification,
            final T expected ) {
        final Class<?> matchedClass =
                matcherSpecification.getAnnotation( MatcherOf.class ).value( );
        if ( expected.getClass( ) != matchedClass )
            throw new IllegalArgumentException( /* TODO */);

        return of( (Class<T>) matchedClass, matcherSpecification, expected );
    }

    public static <T> AnnotationMatcher<T> of( final Class<T> matchedClass, final Class<?> matcherSpecification,
            final T expected ) {
        return new AnnotationMatcher<T>( expected, matchedClass, matcherSpecification );
    }

    // TODO factory method that takes the Class<T> object from the annotation
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
            throw new RuntimeException( e ); // TODO exception
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException( e ); // TODO exception
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
        final Method[ ] methods = matcherSpecification.getMethods( );
        for ( final Method propertyMethod : methods )
        {
            final Method extractor = matchedClass.getMethod( propertyMethod.getName( ) );

            if ( !propertyMethod.getReturnType( ).isAssignableFrom( extractor.getReturnType( ) ) )
                throw new IllegalArgumentException( /* TODO */);

            // TODO expected property binding time
            final Object expectedProperty = extractor.invoke( expected );

            // TODO better default than @Equals
            Matcher<?> propertyMatcher;
            if ( propertyMethod.isAnnotationPresent( Identical.class ) )
                propertyMatcher = sameInstance( expectedProperty );
            else if ( propertyMethod.isAnnotationPresent( Approximate.class ) )
            {
                // TODO type check
                propertyMatcher = closeTo( (Double) expectedProperty,
                    propertyMethod.getAnnotation( Approximate.class ).value( ) );
            }
            else
                propertyMatcher = equalTo( expectedProperty );

            final SubMatcher<T> sub = new SubMatcher<T>( extractor, propertyMatcher );
            subMatchers.add( sub );
        }
    }

    private final T expected;
    private final Class<?> matcherSpecification;
    private final Class<T> matchedClass;

    private final Collection<SubMatcher<T>> subMatchers =
            new ArrayList<SubMatcher<T>>( );

}
