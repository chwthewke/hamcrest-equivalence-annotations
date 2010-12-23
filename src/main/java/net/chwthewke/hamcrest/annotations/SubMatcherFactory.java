package net.chwthewke.hamcrest.annotations;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;

public class SubMatcherFactory {

    public static <T, U> SubMatcherProvider<T, U> equalTo(
            final String propertyName,
            final Function<T, U> propertyMethod ) {

        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return SubMatcherProvider.<T, U>create(
            propertyName,
            propertyMethod,
            equalToMatcherFactory );
    }

    public static <T, U> SubMatcherProvider<T, U> sameInstance(
            final String propertyName,
            final Function<T, U> propertyMethod ) {

        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return SubMatcherProvider.<T, U>create(
            propertyName,
            propertyMethod,
            sameInstanceMatcherFactory );
    }

    public static <T> SubMatcherProvider<T, Double> closeTo(
            final String propertyName,
            final Function<T, Double> propertyMethod,
            final double tolerance ) {
        final Function<Double, Matcher<? super Double>> closeToMatcherFactory =
                new Function<Double, Matcher<? super Double>>( ) {
                    public Matcher<? super Double> apply( final Double expected ) {
                        return Matchers.closeTo( expected, tolerance );
                    }
                };

        return SubMatcherProvider.<T, Double>create(
            propertyName,
            propertyMethod,
            closeToMatcherFactory );
    }
}
