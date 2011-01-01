package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.AnnotationMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.MatcherOf;
import net.chwthewke.hamcrest.annotations.NotPublic;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class NonPublicPropertyMatchTest {
    @Test
    public void matchingInterfaceTargetsAnnotatedPrivateProperty( ) throws Exception {
        // Setup

        // Exercise
        final Matcher<Matched> matcher = asSpecifiedBy(
                SpecificationWithAnnotatedPrivateProperty.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
        // Verify
        assertThat( matcher.matches( new Matched( "test" ) ), is( true ) );
    }

    public static class Matched {

        public Matched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithAnnotatedPrivateProperty {
        @Equality
        @NotPublic
        int getId( );
    }

}
