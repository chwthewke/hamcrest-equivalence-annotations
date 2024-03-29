package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.sut.classes.DerivedFromPrivateProperty;
import net.chwthewke.hamcrest.sut.classes.DerivedWithPackageLocalProperty;
import net.chwthewke.hamcrest.sut.classes.DerivedWithProtectedProperty;
import net.chwthewke.hamcrest.sut.classes.DerivedWithPublicProperty;
import net.chwthewke.hamcrest.sut.classes.WithPackageLocalProperty;
import net.chwthewke.hamcrest.sut.classes.WithPrivateProperty;
import net.chwthewke.hamcrest.sut.classes.WithProtectedProperty;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.classes.isolated.IsolatedDerivedWithPackageLocalProperty;

import org.junit.Before;
import org.junit.Test;

public class FindVisiblePropertyFunctionTest {

    private FindPropertyFunction findPropertyFunction;

    @Before
    public void setupMethodFinder( ) {
        findPropertyFunction = new FindVisiblePropertyFunction( );
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
    public void findProtectedProperty( ) throws Exception {
        // Setup

        // Exercise

        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithProtectedProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( DerivedWithProtectedProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void findBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithProtectedProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( equalTo( WithProtectedProperty.class.getDeclaredMethod( "getIntValue" ) ) ) );

    }

    @Test
    public void findPackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( WithPackageLocalProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( WithPackageLocalProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void findOverridenPackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithPackageLocalProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( DerivedWithPackageLocalProperty.class.getDeclaredMethod( "getValue" ) ) ) );

    }

    @Test
    public void findBasePackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( DerivedWithPackageLocalProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( equalTo( WithPackageLocalProperty.class.getDeclaredMethod( "getIntValue" ) ) ) );

    }

    @Test
    public void dontFindNonLocalBasePackageLocalProperty( ) throws Exception {
        // Setup

        try
        {
            // Exercise
            findPropertyFunction
                .findPropertyMethod( IsolatedDerivedWithPackageLocalProperty.class, String.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "isolated.IsolatedDerivedWithPackageLocalProperty lacks the visible property 'getValue()'." ) ) );
        }
    }

    @Test
    public void findPrivateProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = findPropertyFunction
            .findPropertyMethod( WithPrivateProperty.class, long.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( WithPrivateProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void dontFindPrivatePropertyInSuperclass( ) throws Exception {
        // Setup

        try
        {
            // Exercise
            findPropertyFunction
                .findPropertyMethod( DerivedFromPrivateProperty.class, long.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.sut.classes." +
                        "DerivedFromPrivateProperty lacks the visible property 'getValue()'." ) ) );
        }
    }

    @Test
    public void dontFindMissingMethod( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            findPropertyFunction.findPropertyMethod( Float.class, Object.class, "getFrob" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class java.lang." +
                        "Float lacks the visible property 'getFrob()'." ) ) );
        }
    }

}
