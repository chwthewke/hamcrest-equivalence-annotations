package net.chwthewke.hamcrest.matchers;

import org.junit.Before;
import org.junit.Test;

public class PropertyMethodFinderPublicOnlyTest {

    private PropertyMethodFinder methodFinder;

    @Before
    public void setupMethodFinder( ) {
        methodFinder = new PropertyMethodFinder( );
    }

    @Test
    public void findPublicProperty( ) throws Exception {
        // Setup

        // Exercise

        // Verify

    }

    @Test
    public void findOverridenPublicProperty( ) throws Exception {
        // Setup

        // Exercise

        // Verify

    }

    @Test
    public void findBasePublicProperty( ) throws Exception {
        // Setup

        // Exercise

        // Verify

    }

    @Test
    public void dontFindProtectedProperty( ) throws Exception {
        // Setup

        // Exercise

        // Verify

    }

    @Test
    public void dontFindBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise

        // Verify

    }

}
