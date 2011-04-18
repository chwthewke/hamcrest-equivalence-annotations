package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.equivalences.SimpleEquivalence;
import net.chwthewke.hamcrest.sut.nonpublic.ByNonPublicEquivalence;
import net.chwthewke.hamcrest.sut.specs.ByAbstractSimpleEquivalence;
import net.chwthewke.hamcrest.sut.specs.ByBadEquivalence;
import net.chwthewke.hamcrest.sut.specs.ByNoDefaultCtorEquivalence;
import net.chwthewke.hamcrest.sut.specs.BySimpleEquivalence;
import net.chwthewke.hamcrest.sut.specs.BySimpleStringEquivalence;

import org.junit.Before;
import org.junit.Test;

public class EquivalenceActivatorTest {

    private EquivalenceActivator equivalenceActivator;

    @Before
    public void setupEquivalenceActivator( ) {
        equivalenceActivator = new EquivalenceActivator( );
    }

    @Test
    public void activatorInstanciatesEquivalence( ) throws Exception {
        // Setup
        final Method method = BySimpleEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = equivalenceActivator
            .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
        // Verify
        assertThat( equivalence, is( instanceOf( SimpleEquivalence.class ) ) );
    }

    @Test
    public void activatorFailsToInstanciateNonPublicEquivalence( ) throws Exception {
        // Setup
        final Method method = ByNonPublicEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Bad use of @ByEquivalence: value class " +
                    "net.chwthewke.hamcrest.sut.nonpublic.NonPublicEquivalence " +
                    "must be a public class." ) ) );
        }
    }

    @Test
    public void activatorFailsToInstanciateEquivalenceWithoutADefaultCtor( ) throws Exception {
        // Setup
        final Method method = ByNoDefaultCtorEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Bad use of @ByEquivalence: " +
                    "class net.chwthewke.hamcrest.sut.equivalences.NoDefaultCtorEquivalence " +
                    "must have a public no-arg constructor." ) ) );
        }
    }

    @Test
    public void activatorFailsToInstanciateEquivalenceOfWrongType( ) throws Exception {
        // Setup
        final Method method = BySimpleStringEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Bad use of @ByEquivalence: " +
                    "value net.chwthewke.hamcrest.sut.equivalences.SimpleStringEquivalence " +
                    "seems to implement Equivalence<java.lang.String>, " +
                    "whereas target property has type class java.lang.Integer" ) ) );
        }
    }

    @Test
    public void activatorFailsToInstanciateAbstractEquivalence( ) throws Exception {
        // Setup
        final Method method = ByAbstractSimpleEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Bad use of @ByEquivalence: " +
                    "value class net.chwthewke.hamcrest.sut.equivalences.AbstractSimpleEquivalence " +
                    "cannot be an abstract class." ) ) );
        }
    }

    @Test
    public void activatorReportsExceptionInEquivalenceCtor( ) throws Exception {
        // Setup
        final Method method = ByBadEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), Integer.class );
            // Verify
            fail( );
        }
        catch ( final RuntimeException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "NullPointerException while calling the default constructor " +
                    "of class net.chwthewke.hamcrest.sut.equivalences.BadEquivalence." ) ) );
        }
    }
}
