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

package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

import org.hamcrest.Matcher;
import org.junit.Test;

public class DerivedPropertyMatchTest {

    @Test
    public void matcherOfDerivedProperty( ) throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnDerivedProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        // Exercise
        final boolean match = matcher.matches( new DerivedMatched( "123" ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void mismatchOfDerivedProperty( ) throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnDerivedProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        // Exercise
        final boolean match = matcher.matches( new DerivedMatched( "234" ) );
        // Verify
        assertThat( match, is( false ) );
    }

    @Test
    public void matcherOfOverridenProperty( ) throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnOverridenProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        final DerivedMatched actual = new DerivedMatched( "0" );
        actual.setValue( "123" );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void matchOnBasePropertyUsesBasePropertyOnOverridenType( ) throws Exception {
        // Setup
        final Matcher<Matched> matcher = asSpecifiedBy( SpecificationOnBaseProperty.class )
            .equivalentTo( new Matched( "123" ) );
        final DerivedMatched actual = new DerivedMatched( "0" );
        actual.setValue( "123" );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    public static class DerivedMatched extends Matched {

        public DerivedMatched( final String value ) {
            super( value );
            value0 = value;
        }

        @Override
        public String getValue( ) {
            return value0;
        }

        public void setValue( final String value ) {
            value0 = value;
        }

        private String value0;
    }

    public static class Matched {

        public Matched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        public int getIntValue( ) {
            try
            {
                return Integer.parseInt( value );
            }
            catch ( final NumberFormatException e )
            {
                return Integer.MIN_VALUE;
            }
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @EquivalenceSpecificationOn( DerivedMatched.class )
    public static interface SpecificationOnDerivedProperty extends EquivalenceSpecification<DerivedMatched> {
        @Identity
        int getIntValue( );
    }

    @EquivalenceSpecificationOn( DerivedMatched.class )
    public static interface SpecificationOnOverridenProperty extends EquivalenceSpecification<DerivedMatched> {
        @Equality
        String getValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationOnBaseProperty extends EquivalenceSpecification<Matched> {
        @Equality
        String getValue( );
    }

}
