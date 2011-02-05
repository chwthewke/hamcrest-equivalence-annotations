package net.chwthewke.hamcrest.equivalence;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

import com.google.common.collect.Lists;

public class EquivalenceClassMatchers {

    public static <T> Matcher<Equivalence<?>> equivalenceOf( final Class<?> matchedType ) {
        return new EquivalenceTypeParameterMatcher( matchedType );
    }

    private static class EquivalenceTypeParameterMatcher extends TypeSafeMatcher<Equivalence<?>> {

        EquivalenceTypeParameterMatcher( final Class<?> type ) {
            this.type = type;
        }

        public void describeTo( final Description description ) {
            description
                .appendText( "an instance implementing Equivalence<" )
                .appendText( type.getSimpleName( ) )
                .appendText( ">" );
        }

        @Override
        protected boolean matchesSafely( final Equivalence<?> item ) {
            return typeFinder.findExpectedType( item.getClass( ) ) == type;
        }

        private final Class<?> type;
        private static final ReflectiveTypeFinder typeFinder = new ReflectiveTypeFinder( "equivalentTo", 1, 0 );
    }

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
            for ( final T leftItem : expectedlyEquivalents )
                for ( final T rightItem : expectedlyEquivalents )
                {
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
            this.expectedlyEquivalents = expectedlyEquivalents;
        }

        private final boolean testForEquivalence;
        private final Collection<T> expectedlyEquivalents;

    }
}
