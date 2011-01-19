/*
 * Copyright (c) 2010-2011, Thomas Dufour
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.matchers.EquivalenceSpecification;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ApproximateEqualityMatchersTest {

    @Test
    public void matchApproximateEqualityOnDouble( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( DoubleMatcherSpecification.class )
                    .equivalentTo( new Matched( 1.25d, 0f ) );
        final Matched actual = new Matched( 1.2500005d, 0f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }

    @Test
    public void matchApproximateEqualityOnFloat( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher =
                asSpecifiedBy( FloatMatcherSpecification.class )
                    .equivalentTo( new Matched( 0d, 1.1f ) );
        final Matched actual = new Matched( 0d, 1.0999999f );
        // Exercise

        // Verify
        assertThat( matcher.matches( actual ), is( true ) );
    }

    public static class Matched {

        Matched( final double doubleValue, final float floatValue ) {
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

    @EquivalenceSpecificationOn( Matched.class )
    public static interface DoubleMatcherSpecification extends EquivalenceSpecification<Matched> {
        @ApproximateEquality( tolerance = 0.000001d )
        double getDoubleValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface FloatMatcherSpecification extends EquivalenceSpecification<Matched> {
        @ApproximateEquality( tolerance = 0.000001d )
        float getFloatValue( );
    }
}
