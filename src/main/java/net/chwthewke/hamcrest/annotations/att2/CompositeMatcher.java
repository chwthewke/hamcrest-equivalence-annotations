package net.chwthewke.hamcrest.annotations.att2;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CompositeMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public void describeTo( final Description description ) {
        description
            .appendText( "a " )
            .appendText( expectedType.getSimpleName( ) );

        String subMatcherLeadin = " with";

        for ( final Entry<SubMatcherProvider<T, ?>, Matcher<?>> entry : subMatchers.entrySet( ) )
        {
            description
                .appendText( subMatcherLeadin )
                .appendText( " " )
                .appendText( entry.getKey( ).getPropertyName( ) )
                .appendText( "()=" )
                .appendDescriptionOf( entry.getValue( ) );
            subMatcherLeadin = ",";
        }

    }

    @Override
    protected boolean matchesSafely( final T item, final Description mismatchDescription ) {

        for ( final Entry<SubMatcherProvider<T, ?>, Matcher<?>> entry : subMatchers.entrySet( ) )
        {
            final Matcher<?> matcher = entry.getValue( );
            final SubMatcherProvider<T, ?> matcherProvider = entry.getKey( );

            final Object propertyValue = matcherProvider.extractProperty( item );
            if ( !matcher.matches( propertyValue ) )
            {
                mismatchDescription
                    .appendText( matcherProvider.getPropertyName( ) )
                    .appendText( "() " );
                matcher.describeMismatch( propertyValue, mismatchDescription );

                return false;
            }
        }

        return true;
    }

    CompositeMatcher( final Class<T> expectedType,
            final Collection<SubMatcherProvider<T, ?>> subMatcherProviders,
            final T expected ) {

        super( expectedType );

        this.expectedType = checkNotNull( expectedType );

        for ( final SubMatcherProvider<T, ?> subMatcherProvider : subMatcherProviders )
            subMatchers.put( subMatcherProvider, subMatcherProvider.matcherOf( expected ) );
    }

    private final Class<T> expectedType;
    private final Map<SubMatcherProvider<T, ?>, Matcher<?>> subMatchers = newLinkedHashMap( );

}
