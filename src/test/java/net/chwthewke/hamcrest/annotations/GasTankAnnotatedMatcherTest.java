package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class GasTankAnnotatedMatcherTest {

    private static Matcher<GasTank> gasTankLike( final GasTank expected ) {
        return AnnotationMatcher.of( GasTank.class, GasTankMatching.class, expected );
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
                        + "getVolume()=a numeric value within <1.0E-6> of <1.5>, "
                        + "getHazardLevel()=sameInstance(<l>)";
        assertThat( description.toString( ), is( equalTo( expectedDescription ) ) );
    }
}
