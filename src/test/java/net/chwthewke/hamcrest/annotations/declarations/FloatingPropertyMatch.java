package net.chwthewke.hamcrest.annotations.declarations;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.MatcherOf;
import net.chwthewke.hamcrest.annotations.MatcherSpecification;

public class FloatingPropertyMatch {

    public static class Matched {

        public Matched( final double doubleValue, final float floatValue ) {
            this.doubleValue = doubleValue;
            this.floatValue = floatValue;
        }

        public double getDoubleValue( ) {
            return doubleValue;
        }

        public float getFloatValue( ) {
            return floatValue;
        }

        private final double doubleValue;
        private final float floatValue;
    }

    @MatcherOf( Matched.class )
    public static interface DoubleMatcherSpecification extends MatcherSpecification<FloatingPropertyMatch.Matched> {
        @ApproximateEquality( 0.000001d )
        double getDoubleValue( );
    }

    @MatcherOf( Matched.class )
    public static interface FloatMatcherSpecification extends MatcherSpecification<FloatingPropertyMatch.Matched> {
        @ApproximateEquality( 0.000001d )
        float getFloatValue( );
    }

}
