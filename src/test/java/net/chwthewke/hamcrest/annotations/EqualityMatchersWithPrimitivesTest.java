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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class EqualityMatchersWithPrimitivesTest {

    @Test
    public void createMatcherOfPrimitiveProperties( ) throws Exception {
        // Setup
        // Exercise
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecification.class, Matched.class )
                    .equivalentTo( new Matched( 1, 4 ) );
        // Verify
        final Description description = new StringDescription( );
        matcher.describeTo( description );
        assertThat( description.toString( ), is( equalTo( "a Matched with getFirst()=<1>, getSecond()=<4>" ) ) );
    }

    @Test
    public void matchPrimitivePropertiesWithDifferentBoxes( ) throws Exception {
        // Setup
        final Matched original = new Matched( 1, 43210 );
        final Matched expected = new Matched( 1, 43210 );

        final Method m = Matched.class.getMethod( "getSecond" );
        assertThat( m.invoke( original ) == m.invoke( expected ), is( false ) );

        // Exercise
        final Matcher<Matched> matcher =
                asSpecifiedBy( MatcherSpecification.class, Matched.class )
                    .equivalentTo( original );
        // Verify

        assertThat( matcher.matches( expected ), is( true ) );
    }

    public static class Matched {
        public Matched( final int first, final int second ) {
            this.first = first;
            this.second = second;
        }

        public int getFirst( ) {
            return first;
        }

        public int getSecond( ) {
            return second;
        }

        private final int first;
        private final int second;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface MatcherSpecification {
        @Equality
        int getFirst( );

        @Identity
        int getSecond( );
    }

}
