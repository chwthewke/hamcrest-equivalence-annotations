package net.chwthewke.hamcrest.sample;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class GasTankEquivalenceTest {

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
