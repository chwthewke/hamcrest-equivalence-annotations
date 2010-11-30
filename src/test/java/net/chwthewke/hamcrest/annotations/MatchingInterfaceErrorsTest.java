package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.declarations.MethodWithArgs;
import net.chwthewke.hamcrest.annotations.declarations.MissingMethod;

import org.junit.Test;

public class MatchingInterfaceErrorsTest {

    @Test
    public void matchingInterfaceTargetsMissingMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            AnnotationMatcher.of(
                MissingMethod.Matched.class,
                MissingMethod.MatcherSpecification.class,
                new MissingMethod.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), containsString( "Missing method name()" ) );
        }
    }

    @Test
    public void matchingInterfaceTargetsMethodWithArguments( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            AnnotationMatcher.of(
                MethodWithArgs.Matched.class,
                MethodWithArgs.MatcherSpecification.class,
                new MethodWithArgs.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), containsString( "Missing method getName()" ) );
        }

    }
}
