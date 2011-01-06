package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.matchers.finder.WithNonPropertyMethod;
import net.chwthewke.hamcrest.matchers.finder.WithPublicProperty;

import org.junit.Before;
import org.junit.Test;

public class PropertyMethodFinderTest {

    private PropertyMethodFinder methodFinder;

    @Before
    public void setupMethodFinder( ) {
        methodFinder = new PropertyMethodFinder( );
    }

    @Test
    public void dontFindMissingMethod( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            methodFinder.findPropertyMethod( WithPublicProperty.class, String.class, "getValue0", false );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.matchers.finder." +
                        "WithPublicProperty lacks the property method 'getValue0()'." ) ) );
        }
    }

    @Test
    public void dontFindMethodWithParameters( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            methodFinder.findPropertyMethod( WithNonPropertyMethod.class, String.class, "getValue", false );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.matchers.finder." +
                        "WithNonPropertyMethod lacks the property method 'getValue()'." ) ) );
        }
    }

    @Test
    public void dontFindMethodWithUnexpectedReturnType( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            methodFinder.findPropertyMethod( WithPublicProperty.class, Integer.class, "getValue", false );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The property 'getValue()' on net.chwthewke.hamcrest.matchers.finder." +
                        "WithPublicProperty has return type java.lang.String which is not " +
                        "assignable to java.lang.Integer." ) ) );
        }
    }

}
