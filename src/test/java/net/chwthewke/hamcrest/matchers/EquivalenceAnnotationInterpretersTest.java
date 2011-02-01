package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.matchers.sut.specs.ByEquivalenceOnObject;
import net.chwthewke.hamcrest.matchers.sut.specs.BySpecificationOnProperty;
import net.chwthewke.hamcrest.matchers.sut.specs.EqualityOnString;
import net.chwthewke.hamcrest.matchers.sut.specs.IdentityOnObject;
import net.chwthewke.hamcrest.matchers.sut.specs.IdentityOnPrimitive;

import org.junit.Before;
import org.junit.Test;

public class EquivalenceAnnotationInterpretersTest {

    private EquivalenceAnnotationInterpreters interpreterSelector;

    @Before
    public void setupInterpreterSelector( ) {
        interpreterSelector = new EquivalenceAnnotationInterpreters( );
    }

    @Test
    public void byEquivalenceIsInterpretedWithByEquivalenceInterpreter( ) throws Exception {
        // Setup

        // Exercise
        final EquivalenceAnnotationInterpreter<Object> interpreter =
                interpreterSelector.selectAnnotationInterpreter(
                    ByEquivalence.class,
                    ByEquivalenceOnObject.class.getMethod( "getValue" ),
                    Object.class );
        // Verify
        assertThat( interpreter, is( instanceOf( ByEquivalenceInterpreter.class ) ) );
    }

    @Test
    public void equalityIsInterpretedWithEqualityInterpreter( ) throws Exception {
        // Setup

        // Exercise
        final EquivalenceAnnotationInterpreter<String> interpreter = interpreterSelector
            .selectAnnotationInterpreter( Equality.class, EqualityOnString.class.getMethod( "getValue" ), String.class );
        // Verify
        assertThat( interpreter, is( instanceOf( EqualityInterpreter.class ) ) );
    }

    @Test
    public void identityIsInterpretedWithIdentityInterpreter( ) throws Exception {
        // Setup

        // Exercise
        final EquivalenceAnnotationInterpreter<Object> interpreter = interpreterSelector
            .selectAnnotationInterpreter( Identity.class, IdentityOnObject.class.getMethod( "getValue" ), Object.class );
        // Verify
        assertThat( interpreter, is( instanceOf( IdentityInterpreter.class ) ) );
    }

    @Test
    public void identityOnPrimitiveIsInterpretedWithEqualityInterpreter( ) throws Exception {
        // Setup

        // Exercise
        final EquivalenceAnnotationInterpreter<Integer> interpreter = interpreterSelector
            .selectAnnotationInterpreter( Identity.class,
                IdentityOnPrimitive.class.getMethod( "getIntValue" ), Integer.class );
        // Verify
        assertThat( interpreter, is( instanceOf( EqualityInterpreter.class ) ) );
    }

    @Test
    public void bySpecificationIsInterpretedWithBySpecificationInterpreter( ) throws Exception {
        // Setup

        // Exercise
        final EquivalenceAnnotationInterpreter<WithPublicProperty> interpreter = interpreterSelector
            .selectAnnotationInterpreter( BySpecification.class,
                BySpecificationOnProperty.class.getMethod( "getValue" ), WithPublicProperty.class );
        // Verify
        assertThat( interpreter, is( instanceOf( BySpecificationInterpreter.class ) ) );
    }
}
