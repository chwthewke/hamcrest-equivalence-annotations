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

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
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

    @SuppressWarnings( { "rawtypes" } )
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

        assertThat( (Equivalence) actualEquivalence, is( sameInstance( (Equivalence) token ) ) );
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

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForBooleanArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Boolean> booleanEquivalence = mockEquivalence( );
        doReturn( booleanEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Boolean.class );

        final Equivalence<boolean[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).booleanArrayEquivalence( booleanEquivalence, true );

        final TypeEquivalenceSpecification<boolean[ ]> specification =
                createTypeEquivalenceSpecification( boolean[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super boolean[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Boolean.class );
        verify( equivalenceFactory ).booleanArrayEquivalence( booleanEquivalence, true );

        assertThat( (Equivalence<boolean[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForByteArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Byte> byteEquivalence = mockEquivalence( );
        doReturn( byteEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Byte.class );

        final Equivalence<byte[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).byteArrayEquivalence( byteEquivalence, true );

        final TypeEquivalenceSpecification<byte[ ]> specification =
                createTypeEquivalenceSpecification( byte[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super byte[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Byte.class );
        verify( equivalenceFactory ).byteArrayEquivalence( byteEquivalence, true );

        assertThat( (Equivalence<byte[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForCharacterArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Character> charEquivalence = mockEquivalence( );
        doReturn( charEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Character.class );

        final Equivalence<char[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).charArrayEquivalence( charEquivalence, true );

        final TypeEquivalenceSpecification<char[ ]> specification =
                createTypeEquivalenceSpecification( char[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super char[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Character.class );
        verify( equivalenceFactory ).charArrayEquivalence( charEquivalence, true );

        assertThat( (Equivalence<char[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForDoubleArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final ApproximateEquality equalityAnnotation = mockAnnotation( ApproximateEquality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Number> doubleEquivalence = mockEquivalence( );
        doReturn( doubleEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Double.class );

        final Equivalence<double[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).doubleArrayEquivalence( doubleEquivalence, true );

        final TypeEquivalenceSpecification<double[ ]> specification =
                createTypeEquivalenceSpecification( double[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super double[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Double.class );
        verify( equivalenceFactory ).doubleArrayEquivalence( doubleEquivalence, true );

        assertThat( (Equivalence<double[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForFloatArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final ApproximateEquality equalityAnnotation = mockAnnotation( ApproximateEquality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( false ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Number> floatEquivalence = mockEquivalence( );
        doReturn( floatEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Float.class );

        final Equivalence<float[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).floatArrayEquivalence( floatEquivalence, false );

        final TypeEquivalenceSpecification<float[ ]> specification =
                createTypeEquivalenceSpecification( float[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super float[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Float.class );
        verify( equivalenceFactory ).floatArrayEquivalence( floatEquivalence, false );

        assertThat( (Equivalence<float[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForIntegerArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Integer> intEquivalence = mockEquivalence( );
        doReturn( intEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Integer.class );

        final Equivalence<int[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).intArrayEquivalence( intEquivalence, true );

        final TypeEquivalenceSpecification<int[ ]> specification =
                createTypeEquivalenceSpecification( int[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super int[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Integer.class );
        verify( equivalenceFactory ).intArrayEquivalence( intEquivalence, true );

        assertThat( (Equivalence<int[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForLongArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( false ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Long> longEquivalence = mockEquivalence( );
        doReturn( longEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Long.class );

        final Equivalence<long[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).longArrayEquivalence( longEquivalence, false );

        final TypeEquivalenceSpecification<long[ ]> specification =
                createTypeEquivalenceSpecification( long[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super long[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Long.class );
        verify( equivalenceFactory ).longArrayEquivalence( longEquivalence, false );

        assertThat( (Equivalence<long[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void specificationForShortArrayEquivalenceTransformsEquivalenceOnElementType( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final OnArrayElements arrayAnnotation = mockAnnotation( OnArrayElements.class );

        doReturn( true ).when( arrayAnnotation ).inOrder( );

        final Equivalence<Short> shortEquivalence = mockEquivalence( );
        doReturn( shortEquivalence ).when( basicInterpreter )
            .getEquivalenceFor( equalityAnnotation, Short.class );

        final Equivalence<short[ ]> token = mockEquivalence( );

        doReturn( token ).when( equivalenceFactory ).shortArrayEquivalence( shortEquivalence, true );

        final TypeEquivalenceSpecification<short[ ]> specification =
                createTypeEquivalenceSpecification( short[ ].class, equalityAnnotation, arrayAnnotation );
        // Exercise
        final Equivalence<? super short[ ]> actualEquivalence = interpreter.getEquivalenceFor( specification );
        // Verify
        verify( basicInterpreter ).getEquivalenceFor( equalityAnnotation, Short.class );
        verify( equivalenceFactory ).shortArrayEquivalence( shortEquivalence, true );

        assertThat( (Equivalence<short[ ]>) actualEquivalence, is( sameInstance( token ) ) );
    }

    @Test
    public void interpretUnknownContainerAnnotationFails( ) throws Exception {
        // Setup
        final Equality equalityAnnotation = mockAnnotation( Equality.class );
        final Override invalidContainerAnotation = mockAnnotation( Override.class );

        final TypeEquivalenceSpecification<Object> specification =
                createTypeEquivalenceSpecification( Object.class, equalityAnnotation, invalidContainerAnotation );
        // Exercise
        try
        {
            interpreter.getEquivalenceFor( specification );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Unknown container annotation @Override." ) ) );
        }
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
