package net.chwthewke.hamcrest.annotations;

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

        for ( final Entry<SubMatcherTemplate<T, ?>, Matcher<?>> entry : subMatchers.entrySet( ) )
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

        for ( final Entry<SubMatcherTemplate<T, ?>, Matcher<?>> entry : subMatchers.entrySet( ) )
        {
            final Matcher<?> matcher = entry.getValue( );
            final SubMatcherTemplate<T, ?> matcherTemplate = entry.getKey( );

            final Object propertyValue = matcherTemplate.extractProperty( item );
            if ( !matcher.matches( propertyValue ) )
            {
                mismatchDescription
                    .appendText( matcherTemplate.getPropertyName( ) )
                    .appendText( "() " );
                matcher.describeMismatch( propertyValue, mismatchDescription );

                return false;
            }
        }

        return true;
    }

    CompositeMatcher( final Class<T> expectedType,
            final Collection<SubMatcherTemplate<T, ?>> subMatcherTemplates,
            final T expected ) {

        super( expectedType );

        this.expectedType = checkNotNull( expectedType );

        for ( final SubMatcherTemplate<T, ?> subMatcherTemplate : subMatcherTemplates )
            subMatchers.put( subMatcherTemplate, subMatcherTemplate.specializeFor( expected ) );
    }

    private final Class<T> expectedType;
    private final Map<SubMatcherTemplate<T, ?>, Matcher<?>> subMatchers = newLinkedHashMap( );

}
