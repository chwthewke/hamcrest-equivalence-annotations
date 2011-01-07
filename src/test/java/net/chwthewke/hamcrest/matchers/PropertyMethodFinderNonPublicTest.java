package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.matchers.finder.BaseWithProtectedProperty;
import net.chwthewke.hamcrest.matchers.finder.DerivedWithProtectedProperty;

import org.junit.Before;
import org.junit.Test;

public class PropertyMethodFinderNonPublicTest {

    private PropertyMethodFinder methodFinder;

    @Before
    public void setupMethodFinder( ) {
        methodFinder = new PropertyMethodFinder( );
    }

    @Test
    public void findProtectedProperty( ) throws Exception {
        // Setup

        // Exercise

        final Method method = methodFinder
            .findPropertyMethod( DerivedWithProtectedProperty.class, String.class, "getValue", true );
        // Verify
        assertThat( method, is( equalTo( DerivedWithProtectedProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void findBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithProtectedProperty.class, int.class, "getIntValue", true );
        // Verify
        assertThat( method, is( equalTo( BaseWithProtectedProperty.class.getDeclaredMethod( "getIntValue" ) ) ) );

    }

}
