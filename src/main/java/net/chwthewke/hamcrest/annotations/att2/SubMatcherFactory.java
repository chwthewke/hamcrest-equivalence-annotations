package net.chwthewke.hamcrest.annotations.att2;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;

public class SubMatcherFactory {

    public static <T, U> SubMatcherProvider<T, U> equalTo(
            final String propertyName,
            final Class<U> type,
            final Function<T, U> propertyMethod ) {

        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return new SubMatcherProvider<T, U>(
            propertyName,
            type,
            propertyMethod,
            equalToMatcherFactory );
    }

    public static <T, U> SubMatcherProvider<T, U> sameInstance(
            final String propertyName,
            final Class<U> type,
            final Function<T, U> propertyMethod ) {

        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return new SubMatcherProvider<T, U>(
            propertyName,
            type,
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

        return new SubMatcherProvider<T, Double>(
            propertyName,
            Double.class,
            propertyMethod,
            closeToMatcherFactory );
    }

}
