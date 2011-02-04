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
import net.chwthewke.hamcrest.sut.specs.ByAbstractSimpleEquivalence;
import net.chwthewke.hamcrest.sut.specs.BySimpleEquivalence;

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
            .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), method, Integer.class );
        // Verify
        assertThat( equivalence, is( instanceOf( SimpleEquivalence.class ) ) );
    }

    @Test
    public void activatorFailsToInstanciateAbstractEquivalence( ) throws Exception {
        // Setup
        final Method method = ByAbstractSimpleEquivalence.class.getMethod( "getIntValue" );
        // Exercise
        try
        {
            equivalenceActivator
                .createEquivalenceInstance( method.getAnnotation( ByEquivalence.class ), method, Integer.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "" ) ) );
        }
    }
}
