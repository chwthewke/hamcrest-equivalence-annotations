package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static net.chwthewke.hamcrest.matchers.TypeEquivalenceSpecification.createTypeEquivalenceSpecification;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.ApproximateEqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IdentityEquivalence;
import net.chwthewke.hamcrest.equivalence.LiftedEquivalence;
import net.chwthewke.hamcrest.sut.classes.WithDoubleProperty;
import net.chwthewke.hamcrest.sut.classes.WithObjectProperty;
import net.chwthewke.hamcrest.sut.classes.WithPropertyWithDefinedEquivalence;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.specs.ApproximateEqualityOnDouble;
import net.chwthewke.hamcrest.sut.specs.ByEquivalenceOnObject;
import net.chwthewke.hamcrest.sut.specs.BySpecificationOnProperty;
import net.chwthewke.hamcrest.sut.specs.EqualityOnString;
import net.chwthewke.hamcrest.sut.specs.IdentityOnObject;
import net.chwthewke.hamcrest.sut.specs.IdentityOnPrimitive;
import net.chwthewke.hamcrest.sut.specs.ObjectEqualityEquivalence;
import net.chwthewke.hamcrest.sut.specs.TextIgnoringCaseOnString;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import com.google.common.base.Function;

public class EquivalenceAnnotationProcessorTest {
    @Mock
    private LiftedEquivalenceFactory liftedEquivalenceFactory;

    private AnnotationReader annotationReader;

    @Mock
    private LiftedEquivalence<?, ?> token;

    @Captor
    private ArgumentCaptor<Equivalence<?>> equivalenceCaptor;

    @SuppressWarnings( "unchecked" )
    @Before
    public void setupDependencies( ) {
        initMocks( this );
        when( liftedEquivalenceFactory.create( anyString( ), any( Equivalence.class ), any( Function.class ) ) )
            .thenReturn( token );

        annotationReader = new AnnotationReader( );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedEqualityEquivalence( ) throws Exception {
        // Setup
        final Method specification = EqualityOnString.class.getMethod( "getValue" );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), any( EqualityEquivalence.class ), functionOf( target ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedIdentityEquivalence( ) throws Exception {
        // Setup
        final Method specification = IdentityOnObject.class.getMethod( "getValue" );
        final Method target = WithObjectProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithObjectProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithObjectProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), any( IdentityEquivalence.class ), functionOf( target ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithObjectProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedIdentityEquivalenceOnPrimitive( ) throws Exception {
        // Setup
        final Method specification = IdentityOnPrimitive.class.getMethod( "getIntValue" );
        final Method target = WithPublicProperty.class.getMethod( "getIntValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getIntValue" ), any( EqualityEquivalence.class ), functionOf( target ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedApproximateEqualityEquivalence( ) throws Exception {
        // Setup
        final Method specification = ApproximateEqualityOnDouble.class.getMethod( "getValue" );
        final Method target = WithDoubleProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithDoubleProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithDoubleProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ),
                (Equivalence<Number>) equivalenceCaptor.capture( ),
                EquivalenceAnnotationProcessorTest.<WithDoubleProperty, Double>functionOf( target ) );

        final Equivalence<Number> capturedEquivalence = (Equivalence<Number>) equivalenceCaptor.getValue( );
        assertThat( capturedEquivalence, is( instanceOf( ApproximateEqualityEquivalence.class ) ) );
        assertThat( capturedEquivalence, equates( (Number) 0d, 0.001d ) );
        assertThat( capturedEquivalence, separates( (Number) 0d, 0.0011d ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithDoubleProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedTextEquivalenceOnPrimitive( ) throws Exception {
        // Setup
        final Method specification = TextIgnoringCaseOnString.class.getMethod( "getValue" );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), (Equivalence<String>) equivalenceCaptor.capture( ),
                EquivalenceAnnotationProcessorTest.<WithDoubleProperty, String>functionOf( target ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
        final Equivalence<String> capturedEquivalence = (Equivalence<String>) equivalenceCaptor.getValue( );
        assertThat( capturedEquivalence, equates( "abc", "ABC" ) );
        assertThat( capturedEquivalence, separates( "abc", " abc", "abc " ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedBySpecificationEquivalence( ) throws Exception {
        // Setup
        final Method specification = BySpecificationOnProperty.class.getMethod( "getValue" );
        final Method target = WithPropertyWithDefinedEquivalence.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPropertyWithDefinedEquivalence> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithPropertyWithDefinedEquivalence> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create(
                eq( "getValue" ),
                (Equivalence<WithPublicProperty>) equivalenceCaptor.capture( ),
                EquivalenceAnnotationProcessorTest.<WithPropertyWithDefinedEquivalence, WithPublicProperty>functionOf( target ) );

        final Equivalence<WithPublicProperty> capturedEquivalence = (Equivalence<WithPublicProperty>) equivalenceCaptor.getValue( );
        assertThat( capturedEquivalence, is( instanceOf( CompositeEquivalence.class ) ) );
        assertThat( capturedEquivalence,
            equates( new WithPublicProperty( "abc" ), new WithPublicProperty( "abc" ) ) );
        assertThat( capturedEquivalence,
            separates( new WithPublicProperty( "abc" ), new WithPublicProperty( "def" ) ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPropertyWithDefinedEquivalence>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedByEquivalenceEquivalence( ) throws Exception {
        // Setup
        final Method specification = ByEquivalenceOnObject.class.getMethod( "getValue" );
        final Method target = WithObjectProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithObjectProperty> annotationProcessor =
                newEquivalenceAnnotationProcessor( specification, target );
        // Exercise
        final Equivalence<WithObjectProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), any( ObjectEqualityEquivalence.class ), functionOf( target ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithObjectProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void failsWithNoAnnotationType( ) throws Exception {
        // Setup
        annotationReader = mock( AnnotationReader.class );
        final Method specification = EqualityOnString.class.getMethod( "getValue" );
        when( (TypeEquivalenceSpecification<String>) annotationReader.getTypeEquivalenceSpecification( specification ) )
            .thenReturn( createTypeEquivalenceSpecification( String.class, null, null ) );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );

        // Exercise
        try
        {
            newEquivalenceAnnotationProcessor( specification, target ).processEquivalenceSpecification( );
            // Verify
            fail( );
        }
        catch ( final NullPointerException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Unexpected missing annotation." ) ) );
        }
    }

    private <U> EquivalenceAnnotationProcessor<U> newEquivalenceAnnotationProcessor(
            final Method specification,
            final Method target ) {

        final EquivalenceFactory equivalenceFactory = new EquivalenceFactory( );
        final TypeEquivalenceComputer typeEquivalenceComputer =
                new TypeEquivalenceComputer( equivalenceFactory,
                    new BasicTypeEquivalenceComputer( equivalenceFactory ) );

        return new EquivalenceAnnotationProcessor<U>( annotationReader,
            typeEquivalenceComputer, liftedEquivalenceFactory, specification, target );
    }

    private static <T, V> Function<T, V> functionOf( final Method target ) {
        return argThat( new TypeSafeMatcher<Function<T, V>>( ) {

            public void describeTo( final Description description ) {
                description.appendText( "Function for " )
                    .appendValue( target );
            }

            @Override
            protected boolean matchesSafely( final Function<T, V> item ) {
                if ( !( item instanceof ReadPropertyFunction ) )
                    return false;

                final ReadPropertyFunction<T, V> propertyFunction = (ReadPropertyFunction<T, V>) item;

                return target.equals( propertyFunction.getProperty( ) );
            }
        } );
    }
}
