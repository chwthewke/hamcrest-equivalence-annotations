package net.chwthewke.hamcrest.visibility;

import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

import org.hamcrest.Matcher;
import org.junit.Test;

public class PackageLocalClassAccessTest {

    @Test
    public void publicPropertyOfAccessiblePackageLocalClassIsMatched( ) throws Exception {
        // Setup
        final Matcher<PackageLocal> matcher =
                asSpecifiedBy( PackageLocalMatchingSpecification.class, PackageLocal.class )
                    .equivalentTo( new PackageLocal( "123" ) );
        // Exercise
        final boolean match = matcher.matches( new PackageLocal( "123" ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @EquivalenceSpecificationOn( PackageLocal.class )
    public static interface PackageLocalMatchingSpecification {
        @Equality
        String getValue( );
    }
}
