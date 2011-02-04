package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.sut.classes.WithNonPropertyMethod;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.junit.Before;
import org.junit.Test;

public class FindPropertyFunctionTest {

    private FindPropertyFunction findPropertyFunction;

    @Before
    public void setupMethodFinder( ) {
        findPropertyFunction = new FindPublicPropertyFunction( );
    }

    @Test
    public void dontFindMissingMethod( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( WithPublicProperty.class, String.class, "getValue0" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "WithPublicProperty lacks the public property 'getValue0()'." ) ) );
        }
    }

    @Test
    public void dontFindMethodWithParameters( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( WithNonPropertyMethod.class, String.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "WithNonPropertyMethod lacks the public property 'getValue()'." ) ) );
        }
    }

    @Test
    public void dontFindMethodWithUnexpectedReturnType( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( WithPublicProperty.class, Integer.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The property 'getValue()' on net.chwthewke.hamcrest.sut.classes." +
                        "WithPublicProperty has return type java.lang.String which is not " +
                        "assignable to java.lang.Integer." ) ) );
        }
    }

    @Test
    public void findCovariantMethod( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction.findPropertyMethod(
            WithPublicProperty.class, Object.class, "getValue" );

        // Verify
        assertThat( method, is( WithPublicProperty.class.getDeclaredMethod( "getValue" ) ) );
    }

}
