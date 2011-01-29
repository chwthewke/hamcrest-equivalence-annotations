package net.chwthewke.hamcrest.matchers;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Lists;

public class EquivalenceClassMatchers {

    public static <T> Matcher<Equivalence<T>> equates( final T first, final T second, final T... others ) {
        return new EquatesMatcher<T>( Lists.asList( first, second, others ) );
    }

    private static class EquatesMatcher<T> extends TypeSafeDiagnosingMatcher<Equivalence<T>> {

        public void describeTo( final Description description ) {
            description
                .appendText( "an equivalence making " )
                .appendValueList( "[", ", ", "]", expectedlyEquivalents )
                .appendText( " equivalent." );
        }

        @Override
        protected boolean matchesSafely( final Equivalence<T> item, final Description mismatchDescription ) {
            boolean match = true;
            for ( final T leftItem : expectedlyEquivalents )
                for ( final T rightItem : expectedlyEquivalents )
                {
                    if ( item.equivalentTo( leftItem ).matches( rightItem ) )
                        continue;

                    if ( !match )
                        mismatchDescription.appendText( "\n" );
                    mismatchDescription
                        .appendValue( leftItem )
                        .appendText( " !~ " )
                        .appendValue( rightItem );

                    match = false;
                }
            return match;
        }

        EquatesMatcher( final Collection<T> expectedlyEquivalents ) {
            this.expectedlyEquivalents = expectedlyEquivalents;
        }

        private final Collection<T> expectedlyEquivalents;

    }
}
