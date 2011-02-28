package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.TypeEquivalenceSpecification.createTypeEquivalenceSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TypeEquivalenceInterpreterTest {

    private BasicTypeEquivalenceInterpreter basicInterpreter;
    private EquivalenceFactory equivalenceFactory;
    private TypeEquivalenceInterpreter interpreter;

    @Before
    public void setupInterpreter( ) {
        basicInterpreter = mock( BasicTypeEquivalenceInterpreter.class );
        equivalenceFactory = mock( EquivalenceFactory.class );

        interpreter = new TypeEquivalenceInterpreter( equivalenceFactory, basicInterpreter );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationOnBasicTypeIsInterpretedByDelegate( ) throws Exception {
        // Setup
        final Equality equality = mockAnnotation( Equality.class );
        final TypeEquivalenceSpecification<String> specification =
                createTypeEquivalenceSpecification( String.class, equality, null );

        final Equivalence<String> token = mock( Equivalence.class );
        doReturn( token ).
            when( basicInterpreter ).getEquivalenceFor( equality, String.class );

        // Exercise
        final Equivalence<? super String> equivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equality, String.class );
        verifyZeroInteractions( equivalenceFactory );
        assertThat( (Equivalence<String>) equivalence, is( sameInstance( token ) ) );
    }

    @Test
    @Ignore
    public void specificationForArrayEquivalenceOnNonArrayTypeThrowsException( ) throws Exception {
        // Setup
        final Equality equality = mockAnnotation( Equality.class );

        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        final TypeEquivalenceSpecification<String> specification =
                createTypeEquivalenceSpecification( String.class, equality, arrayAnnotation );

        // Exercise
        interpreter.getEquivalenceFor( specification );
        // Verify
        fail( );

    }

    private <A extends Annotation> A mockAnnotation( final Class<A> annotationType ) {
        final A annotation = mock( annotationType );
        doReturn( annotationType ).when( annotation ).annotationType( );
        return annotation;
    }
}
