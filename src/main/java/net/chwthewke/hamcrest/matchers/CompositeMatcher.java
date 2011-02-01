package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

class CompositeMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public void describeTo( final Description description ) {
        description
            .appendText( "a " )
            .appendText( expectedType.getSimpleName( ) );

        String subMatcherLeadin = " with";

        for ( final Entry<PropertyEquivalence<T, ?>, Matcher<?>> entry : expectedProperties.entrySet( ) )
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

        for ( final Entry<PropertyEquivalence<T, ?>, Matcher<?>> entry : expectedProperties.entrySet( ) )
        {
            final Matcher<?> matcher = entry.getValue( );
            final PropertyEquivalence<T, ?> propertyEquivalence = entry.getKey( );

            final Object propertyValue = propertyEquivalence.extractPropertyValue( item );
            if ( !matcher.matches( propertyValue ) )
            {
                mismatchDescription
                    .appendText( propertyEquivalence.getPropertyName( ) )
                    .appendText( "() " );
                matcher.describeMismatch( propertyValue, mismatchDescription );

                return false;
            }
        }

        return true;
    }

    CompositeMatcher( final Class<T> expectedType,
            final Collection<PropertyEquivalence<T, ?>> propertyEquivalences,
            final T expected ) {

        super( expectedType );

        this.expectedType = checkNotNull( expectedType );

        for ( final PropertyEquivalence<T, ?> propertyEquivalence : propertyEquivalences )
            expectedProperties.put( propertyEquivalence, propertyEquivalence.specializeFor( expected ) );
    }

    private final Class<T> expectedType;
    private final Map<PropertyEquivalence<T, ?>, Matcher<?>> expectedProperties = newLinkedHashMap( );

}
