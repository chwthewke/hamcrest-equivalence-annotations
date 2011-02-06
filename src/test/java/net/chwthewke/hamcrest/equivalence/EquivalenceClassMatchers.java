package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.google.common.collect.Lists;

public class EquivalenceClassMatchers {

    public static <T> Matcher<Equivalence<T>> equates( final T first, final T second, final T... others ) {
        return new EquatesMatcher<T>( true, Lists.asList( first, second, others ) );
    }

    public static <T> Matcher<Equivalence<T>> separates( final T first, final T second, final T... others ) {
        return new EquatesMatcher<T>( false, Lists.asList( first, second, others ) );
    }

    private static class EquatesMatcher<T> extends TypeSafeDiagnosingMatcher<Equivalence<T>> {

        public void describeTo( final Description description ) {
            description
                .appendText( "an equivalence making " )
                .appendValueList( "[", ", ", "]", expectedlyEquivalents )
                .appendText( testForEquivalence ? " equivalent." : " different." );
        }

        @Override
        protected boolean matchesSafely( final Equivalence<T> item, final Description mismatchDescription ) {
            boolean match = true;

            final int size = expectedlyEquivalents.size( );
            for ( int i = 0; i < size; ++i )
                for ( int j = 0; j < i; ++j )
                {

                    final T leftItem = expectedlyEquivalents.get( j );
                    final T rightItem = expectedlyEquivalents.get( i );

                    if ( !testForEquivalence ^ item.equivalentTo( leftItem ).matches( rightItem ) )
                        continue;

                    if ( !match )
                        mismatchDescription.appendText( "\n" );
                    mismatchDescription
                        .appendValue( leftItem )
                        .appendText( testForEquivalence ? " !~ " : " ~ " )
                        .appendValue( rightItem );

                    match = false;
                }
            return match;
        }

        EquatesMatcher( final boolean testForEquivalence, final Collection<T> expectedlyEquivalents ) {
            this.testForEquivalence = testForEquivalence;
            this.expectedlyEquivalents = newArrayList( expectedlyEquivalents );
        }

        private final boolean testForEquivalence;
        private final List<T> expectedlyEquivalents;

    }
}
