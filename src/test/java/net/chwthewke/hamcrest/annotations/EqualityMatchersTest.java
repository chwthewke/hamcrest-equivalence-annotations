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

import org.junit.Test;

public class EqualityMatchersTest {

    @Test
    public void identicalReferencesMatch( ) throws Exception {
        // Setup
        final IntHolder ref = new IntHolder( 12 );
        final Matched first = new Matched( ref );
        final Matched second = new Matched( ref );
        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void equalButNotIdenticalReferencesAlsoMatch( ) throws Exception {
        // Setup
        final Matched first = new Matched( new IntHolder( 12 ) );
        final Matched second = new Matched( new IntHolder( 12 ) );

        assertThat( first.getHolder( ).equals( second.getHolder( ) ), is( true ) );

        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( true ) );
    }

    public static class Matched {

        public Matched( final IntHolder holder ) {
            this.holder = holder;
        }

        public IntHolder getHolder( ) {
            return holder;
        }

        private final IntHolder holder;
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface MatchingSpecification extends EquivalenceSpecification<Matched> {
        @Equality
        IntHolder getHolder( );
    }

    public static class IntHolder {
        public IntHolder( final int value ) {
            this.value = value;
        }

        @Override
        public int hashCode( ) {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals( final Object obj ) {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass( ) != obj.getClass( ) )
                return false;
            final IntHolder other = (IntHolder) obj;
            if ( value != other.value )
                return false;
            return true;
        }

        private final int value;
    }
}
