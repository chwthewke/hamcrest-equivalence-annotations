package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.TypeEquivalenceSpecification.createTypeEquivalenceSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.annotations.Text;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.junit.Before;
import org.junit.Test;

public class TypeEquivalenceInterpreterTest {

    private BasicTypeEquivalenceInterpreter basicInterpreter;
    private ContainerEquivalenceFactory equivalenceFactory;
    private TypeEquivalenceInterpreter interpreter;

    @Before
    public void setupInterpreter( ) {
        basicInterpreter = mock( BasicTypeEquivalenceInterpreter.class );
        equivalenceFactory = mock( ContainerEquivalenceFactory.class );

        interpreter = new TypeEquivalenceInterpreter( equivalenceFactory, basicInterpreter );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationOnBasicTypeIsInterpretedByDelegate( ) throws Exception {
        // Setup
        final Equality equality = mockAnnotation( Equality.class );
        final TypeEquivalenceSpecification<String> specification =
                createTypeEquivalenceSpecification( String.class, equality, null );

        final Equivalence<String> token = mockEquivalence( );
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
    public void specificationForIterableEquivalenceOnNonIterableTypeThrowsException( ) throws Exception {
        // Setup
        final Identity identity = mockAnnotation( Identity.class );
        final OnIterableElements iterableAnnotation = mockAnnotation( OnIterableElements.class );

        final TypeEquivalenceSpecification<String> specification =
                createTypeEquivalenceSpecification( String.class, identity, iterableAnnotation );
        // Exercise
        try
        {
            interpreter.getEquivalenceFor( specification );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "'specification.getTargetType( )' must implement Iterable." ) ) );
        }
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Test
    public void specificationForIterableEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Text textAnnotation = mockAnnotation( Text.class );
        final OnIterableElements iterableAnnotation = mockAnnotation( OnIterableElements.class );

        doReturn( String.class ).when( iterableAnnotation ).elementType( );
        doReturn( false ).when( iterableAnnotation ).inOrder( );

        final Equivalence<String> textEquivalence = mockEquivalence( );
        doReturn( textEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( textAnnotation, String.class );

        final Equivalence<Iterable<? extends String>> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).iterableEquivalence( textEquivalence, false );

        final TypeEquivalenceSpecification<ArrayList> specification =
                createTypeEquivalenceSpecification( ArrayList.class, textAnnotation, iterableAnnotation );
        // Exercise
        final Equivalence<? super ArrayList> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( textAnnotation, String.class );
        verify( equivalenceFactory ).iterableEquivalence( textEquivalence, false );

        assertThat( (Equivalence<Iterable<? extends String>>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @Test
    public void specificationForArrayEquivalenceOnNonArrayTypeThrowsException( ) throws Exception {
        // Setup
        final Equality equality = mockAnnotation( Equality.class );

        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        final TypeEquivalenceSpecification<String> specification =
                createTypeEquivalenceSpecification( String.class, equality, arrayAnnotation );

        // Exercise
        try
        {
            interpreter.getEquivalenceFor( specification );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "'specification.getTargetType( )' must be an array type." ) ) );
        }

    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Text textAnnotation = mockAnnotation( Text.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( false ).when( arrayAnnotation ).inOrder( );

        final Equivalence<String> textEquivalence = mockEquivalence( );
        doReturn( textEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( textAnnotation, String.class );

        final Equivalence<String[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).arrayEquivalence( textEquivalence, false );

        final TypeEquivalenceSpecification<String[ ]> specification =
                createTypeEquivalenceSpecification( String[ ].class, textAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super String[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( textAnnotation, String.class );
        verify( equivalenceFactory ).arrayEquivalence( textEquivalence, false );

        assertThat( (Equivalence<String[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    private <A extends Annotation> A mockAnnotation( final Class<A> annotationType ) {
        final A annotation = mock( annotationType );
        doReturn( annotationType ).when( annotation ).annotationType( );
        return annotation;
    }

    @SuppressWarnings( "unchecked" )
    private <T> Equivalence<T> mockEquivalence( ) {
        return mock( Equivalence.class );
    }
}
