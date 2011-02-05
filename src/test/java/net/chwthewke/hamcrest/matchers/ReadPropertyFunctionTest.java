package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.sut.classes.WithBadProperty;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.junit.Test;

public class ReadPropertyFunctionTest {

    @Test
    public void functionReadsProperty( ) throws Exception {
        // Setup
        final ReadPropertyFunction<WithPublicProperty, String> function =
                new ReadPropertyFunction<WithPublicProperty, String>(
                    WithPublicProperty.class.getMethod( "getValue" ), String.class );
        final WithPublicProperty source = new WithPublicProperty( "abc" );
        // Exercise
        final String value = function.apply( source );
        // Verify
        assertThat( value, is( equalTo( "abc" ) ) );
    }

    @Test
    public void functionReportsExceptionInProperty( ) throws Exception {
        // Setup
        final ReadPropertyFunction<WithBadProperty, String> function =
                new ReadPropertyFunction<WithBadProperty, String>(
                    WithBadProperty.class.getMethod( "getValue" ), String.class );
        final WithBadProperty source = new WithBadProperty( "abc" );
        // Exercise
        try
        {
            function.apply( source );
            // Verify
            fail( );
        }
        catch ( final RuntimeException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Exception while reading " +
                    "property getValue on instance of " +
                    "net.chwthewke.hamcrest.sut.classes.WithBadProperty." ) ) );
        }
    }

    @Test
    public void functionReportsWrongPropertyType( ) throws Exception {
        // Setup
        final ReadPropertyFunction<WithPublicProperty, Integer> function =
                new ReadPropertyFunction<WithPublicProperty, Integer>(
                        WithPublicProperty.class.getMethod( "getValue" ), Integer.class );
        final WithPublicProperty source = new WithPublicProperty( "abc" );
        // Exercise
        try
        {
            function.apply( source );
            // Verify
            fail( );
        }
        catch ( final RuntimeException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Cannot cast result of property 'getValue()' " +
                    "on instance of net.chwthewke.hamcrest.sut.classes.WithPublicProperty to java.lang.Integer, " +
                    "actual type is java.lang.String." ) ) );
        }
    }
}
