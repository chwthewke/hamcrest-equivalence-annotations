package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.matchers.finder.BaseWithProtectedProperty;
import net.chwthewke.hamcrest.matchers.finder.DerivedWithProtectedProperty;
import net.chwthewke.hamcrest.matchers.finder.DerivedWithPublicProperty;
import net.chwthewke.hamcrest.matchers.finder.WithPublicProperty;

import org.junit.Before;
import org.junit.Test;

public class PropertyMethodFinderPublicOnlyTest {

    private PropertyMethodFinder methodFinder;

    @Before
    public void setupMethodFinder( ) {
        methodFinder = new PropertyMethodFinder( );
    }

    // TODO migrate other failure cases from CMFT

    @Test
    public void findPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( WithPublicProperty.class, String.class, "getValue", false );
        // Verify
        assertThat( method, is( WithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findOverridenPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPublicProperty.class, String.class, "getValue", false );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findBasePublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPublicProperty.class, int.class, "getIntValue", false );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getIntValue" ) ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void dontFindProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        methodFinder.findPropertyMethod( DerivedWithProtectedProperty.class, String.class, "getValue", false );

        // Verify
        // TODO better exception matching
    }

    @Test( expected = IllegalArgumentException.class )
    public void dontFindBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        methodFinder.findPropertyMethod( BaseWithProtectedProperty.class, Integer.class, "getIntValue", false );

        // Verify
        // TODO better exception matching

    }

}
