package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.sut.classes.DerivedWithProtectedProperty;
import net.chwthewke.hamcrest.sut.classes.DerivedWithPublicProperty;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.junit.Before;
import org.junit.Test;

public class FindPublicPropertyFunctionTest {

    private FindPropertyFunction findPropertyFunction;

    @Before
    public void setupMethodFinder( ) {
        findPropertyFunction = new FindPublicPropertyFunction( );
    }

    @Test
    public void findPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( WithPublicProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( WithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findOverridenPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithPublicProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findBasePublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithPublicProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getIntValue" ) ) );
    }

    @Test
    public void dontFindProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( DerivedWithProtectedProperty.class, String.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "DerivedWithProtectedProperty lacks the public property 'getValue()'." ) ) );
        }

    }

    @Test
    public void dontFindBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( DerivedWithProtectedProperty.class, int.class, "getIntValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "DerivedWithProtectedProperty lacks the public property 'getIntValue()'." ) ) );
        }

    }

}
