package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.annotations.CompositeMatcherFactory.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.declarations.IncompatibleReturnTypes;
import net.chwthewke.hamcrest.annotations.declarations.MethodWithArgs;
import net.chwthewke.hamcrest.annotations.declarations.MissingMethod;

import org.junit.Test;

public class MatchingInterfaceSignatureTest {

    @Test
    public void matchingInterfaceTargetsMissingMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            asSpecifiedBy( MissingMethod.MatcherSpecification.class, MissingMethod.Matched.class )
                .equivalentTo( new MissingMethod.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.annotations.declarations.MissingMethod$Matched lacks the property method 'name()' present on net.chwthewke.hamcrest.annotations.declarations.MissingMethod$MatcherSpecification." ) ) );
        }
    }

    @Test
    public void matchingInterfaceTargetsMethodWithArguments( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy( MethodWithArgs.MatcherSpecification.class, MethodWithArgs.Matched.class )
                .equivalentTo( new MethodWithArgs.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.annotations.declarations.MethodWithArgs$Matched lacks the property method 'getName()' present on net.chwthewke.hamcrest.annotations.declarations.MethodWithArgs$MatcherSpecification." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasIncompatibleReturnType( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy( IncompatibleReturnTypes.MatcherSpecification.class, IncompatibleReturnTypes.Matched.class )
                .equivalentTo( new IncompatibleReturnTypes.Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The property 'getName()' on net.chwthewke.hamcrest.annotations.declarations.IncompatibleReturnTypes$Matched has return type java.lang.String which is not assignable to java.lang.Double as specified on net.chwthewke.hamcrest.annotations.declarations.IncompatibleReturnTypes$MatcherSpecification." ) ) );
        }
    }

}
