package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

class CompositeMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public void describeTo( final Description description ) {
        description
            .appendText( "a " )
            .appendText( expectedType.getSimpleName( ) );

        String subMatcherLeadin = " with ";

        for ( final Matcher<?> property : expectedProperties )
        {
            description
                .appendText( subMatcherLeadin )
                .appendDescriptionOf( property );
            subMatcherLeadin = ", ";
        }

    }

    @Override
    protected boolean matchesSafely( final T item, final Description mismatchDescription ) {

        for ( final Matcher<?> property : expectedProperties )
        {
            if ( !property.matches( item ) )
            {
                property.describeMismatch( item, mismatchDescription );
                return false;
            }
        }

        return true;
    }

    CompositeMatcher( final Class<T> expectedType,
            final Collection<Equivalence<T>> propertyEquivalences,
            final T expected ) {

        super( expectedType );

        this.expectedType = checkNotNull( expectedType );

        for ( final Equivalence<T> equivalence : propertyEquivalences )
        {
            expectedProperties.add( equivalence.equivalentTo( expected ) );
        }
    }

    private final Class<T> expectedType;
    private final Collection<Matcher<? super T>> expectedProperties = newArrayList( );

}
