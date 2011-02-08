package net.chwthewke.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class MatcherUtils {
    public static String describe( final Matcher<?> matcher ) {
        final Description description = new StringDescription( );
        matcher.describeTo( description );
        return description.toString( );
    }

    public static <T> String describeMismatch( final Matcher<? super T> matcher, final T item ) {
        final Description description = new StringDescription( );
        matcher.describeMismatch( item, description );
        return description.toString( );
    }

    private MatcherUtils( ) {
    }
}
