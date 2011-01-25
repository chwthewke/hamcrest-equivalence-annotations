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

package net.chwthewke.hamcrest.sample;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class GasTankAnnotatedMatcherTest {

    private static Matcher<GasTank> gasTankLike( final GasTank expected ) {
        return asSpecifiedBy( GasTankEquivalence.class, GasTank.class ).equivalentTo( expected );
    }

    @Test
    public void matcherDescriptionContainsAllProperties( ) throws Exception {
        // Setup
        final GasTank expected = new GasTank( "Argon", 1.5d, GasTank.Hazard.LOW );
        final Matcher<GasTank> matcher = gasTankLike( expected );
        final Description description = new StringDescription( );
        // Exercise
        matcher.describeTo( description );
        // Verify
        final String expectedDescription = "a GasTank with getGas()=\"Argon\", "
                + "getHazardLevel()=sameInstance(<l>), "
                + "getVolume()=a numeric value within <1.0E-6> of <1.5>";
        assertThat( description.toString( ), is( equalTo( expectedDescription ) ) );
    }

    @Test
    public void matcherPassesOnEqualGasTanks( ) throws Exception {
        // Setup
        final GasTank expected = new GasTank( "Hydrogen", 10d, GasTank.Hazard.HIGH );
        final GasTank actual = new GasTank( "Hydrogen", 10.0000008d, GasTank.Hazard.HIGH );
        final Matcher<GasTank> matcher = gasTankLike( expected );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void matcherDiscriminatesOnGas( ) throws Exception {
        // Setup
        final GasTank expected = new GasTank( "Hydrogen", 10d, GasTank.Hazard.HIGH );
        final GasTank actual = new GasTank( "Oxygen", 10.0000008d, GasTank.Hazard.HIGH );
        final Matcher<GasTank> matcher = gasTankLike( expected );
        final Description mismatchDescription = new StringDescription( );
        // Exercise
        final boolean match = matcher.matches( actual );
        matcher.describeMismatch( actual, mismatchDescription );
        // Verify
        assertThat( match, is( false ) );
        assertThat( mismatchDescription.toString( ), is( equalTo( "getGas() was \"Oxygen\"" ) ) );
    }

    @Test
    public void matcherDiscriminatesOnVolume( ) throws Exception {
        // Setup
        final GasTank expected = new GasTank( "Hydrogen", 10d, GasTank.Hazard.HIGH );
        final GasTank actual = new GasTank( "Hydrogen", 11d, GasTank.Hazard.HIGH );
        final Matcher<GasTank> matcher = gasTankLike( expected );
        final Description mismatchDescription = new StringDescription( );
        // Exercise
        final boolean match = matcher.matches( actual );
        matcher.describeMismatch( actual, mismatchDescription );
        // Verify
        assertThat( match, is( false ) );
        final String expectedMismatchDescription = "getVolume() <11.0> differed by <0.999999>";
        assertThat( mismatchDescription.toString( ), is( equalTo( expectedMismatchDescription ) ) );
    }

    @Test
    public void matcherDiscriminatesOnHazard( ) throws Exception {
        // Setup
        final GasTank expected = new GasTank( "Hydrogen", 10d, GasTank.Hazard.HIGH );
        final GasTank actual = new GasTank( "Hydrogen", 10d, new GasTank.Hazard( "h" ) );

        assertThat( actual.getHazardLevel( ), is( equalTo( expected.getHazardLevel( ) ) ) );

        final Matcher<GasTank> matcher = gasTankLike( expected );
        final Description mismatchDescription = new StringDescription( );
        // Exercise
        final boolean match = matcher.matches( actual );
        matcher.describeMismatch( actual, mismatchDescription );
        // Verify
        assertThat( match, is( false ) );
        final String expectedMismatchDescription = "getHazardLevel() was <h>";
        assertThat( mismatchDescription.toString( ), is( equalTo( expectedMismatchDescription ) ) );
    }
}
