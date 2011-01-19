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
import net.chwthewke.hamcrest.matchers.EquivalenceSpecification;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class BySpecificationTest {

    @Test
    public void matchBySpecificationOnProperty( ) throws Exception {
        // Setup
        final OuterMatched expected = new OuterMatched( new InnerMatched( "abcd" ) );
        final OuterMatched actual = new OuterMatched( new InnerMatched( "abcd" ) );
        final Matcher<OuterMatched> matcher = asSpecifiedBy( OuterSpecification.class )
            .equivalentTo( expected );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void mismatchBySpecificationOnProperty( ) throws Exception {
        // Setup
        final OuterMatched expected = new OuterMatched( new InnerMatched( "abcd" ) );
        final OuterMatched actual = new OuterMatched( new InnerMatched( "abcde" ) );
        final Matcher<OuterMatched> matcher = asSpecifiedBy( OuterSpecification.class )
            .equivalentTo( expected );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( false ) );
        final Description mismatchDescription = new StringDescription( );
        matcher.describeMismatch( actual, mismatchDescription );
        assertThat( mismatchDescription.toString( ),
            is( equalTo( "getOuterValue() getInnerValue() was \"abcde\"" ) ) );
    }

    @EquivalenceSpecificationOn( OuterMatched.class )
    public static interface OuterSpecification extends EquivalenceSpecification<OuterMatched> {
        @BySpecification( InnerSpecification.class )
        InnerMatched getOuterValue( );
    }

    public static class OuterMatched {
        public OuterMatched( final InnerMatched value ) {
            this.value = value;
        }

        public InnerMatched getOuterValue( ) {
            return value;
        }

        private final InnerMatched value;
    }

    @EquivalenceSpecificationOn( InnerMatched.class )
    public static interface InnerSpecification extends EquivalenceSpecification<InnerMatched> {
        @Equality
        String getInnerValue( );
    }

    public static class InnerMatched {
        public InnerMatched( final String value ) {
            this.value = value;
        }

        public String getInnerValue( ) {
            return value;
        }

        private final String value;
    }
}
