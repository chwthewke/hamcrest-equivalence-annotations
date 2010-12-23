package net.chwthewke.hamcrest.annotations.att2;

import static net.chwthewke.hamcrest.annotations.att2.CompositeMatcherFactory.matcherBySpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.declarations.IncompatibleReturnTypes;
import net.chwthewke.hamcrest.annotations.declarations.MethodWithArgs;
import net.chwthewke.hamcrest.annotations.declarations.MissingMethod;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MatchingInterfaceSignatureTest {

    @Test
    public void matchingInterfaceTargetsMissingMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            matcherBySpecification(
                MissingMethod.Matched.class,
                MissingMethod.MatcherSpecification.class )
                .of( new MissingMethod.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), startsWith( "Missing method name()" ) );
        }
    }

    @Test
    public void matchingInterfaceTargetsMethodWithArguments( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            matcherBySpecification(
                MethodWithArgs.Matched.class,
                MethodWithArgs.MatcherSpecification.class )
                .of( new MethodWithArgs.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), startsWith( "Missing method getName()" ) );
        }
    }

    @Test
    public void matchingInterfaceHasIncompatibleReturnType( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            matcherBySpecification(
                IncompatibleReturnTypes.Matched.class,
                IncompatibleReturnTypes.MatcherSpecification.class )
                .of( new IncompatibleReturnTypes.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                startsWith( "Incompatible return types: getName: class java.lang.Double vs. class java.lang.String" ) );
        }
    }

}
