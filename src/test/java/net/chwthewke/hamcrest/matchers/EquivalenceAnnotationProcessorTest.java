package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
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
import static org.mockito.Mockito.doReturn;
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

    private EquivalenceFactory equivalenceFactory;
    private AnnotationTypeReader annotationTypeReader;

    @Mock
    private LiftedEquivalence<?, ?> token;

    @Captor
    private ArgumentCaptor<Equivalence<?>> equivalenceCaptor;

    @SuppressWarnings( "unchecked" )
    @Before
    public void setupDependencies( ) {
        initMocks( this );
        when( liftedEquivalenceFactory.create( anyString( ), any( Function.class ), any( Equivalence.class ) ) )
            .thenReturn( token );

        equivalenceFactory = new EquivalenceFactory( );
        annotationTypeReader = new AnnotationTypeReader( );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedEqualityEquivalence( ) throws Exception {
        // Setup
        final Method specification = EqualityOnString.class.getMethod( "getValue" );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithPublicProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                        specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), functionOf( target ), any( EqualityEquivalence.class ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedIdentityEquivalence( ) throws Exception {
        // Setup
        final Method specification = IdentityOnObject.class.getMethod( "getValue" );
        final Method target = WithObjectProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithObjectProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithObjectProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                    specification, target );
        // Exercise
        final Equivalence<WithObjectProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), functionOf( target ), any( IdentityEquivalence.class ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithObjectProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedIdentityEquivalenceOnPrimitive( ) throws Exception {
        // Setup
        final Method specification = IdentityOnPrimitive.class.getMethod( "getIntValue" );
        final Method target = WithPublicProperty.class.getMethod( "getIntValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithPublicProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                    specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getIntValue" ), functionOf( target ), any( EqualityEquivalence.class ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedApproximateEqualityEquivalence( ) throws Exception {
        // Setup
        final Method specification = ApproximateEqualityOnDouble.class.getMethod( "getValue" );
        final Method target = WithDoubleProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithDoubleProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithDoubleProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                    specification, target );
        // Exercise
        final Equivalence<WithDoubleProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ),
                EquivalenceAnnotationProcessorTest.<WithDoubleProperty, Double>functionOf( target ),
                (Equivalence<Number>) equivalenceCaptor.capture( ) );

        final Equivalence<Number> capturedEquivalence = (Equivalence<Number>) equivalenceCaptor.getValue( );
        assertThat( capturedEquivalence, is( instanceOf( ApproximateEqualityEquivalence.class ) ) );
        assertThat( capturedEquivalence, equates( (Number) 0d, 0.001d ) );
        assertThat( capturedEquivalence, separates( (Number) 0d, 0.0011d ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithDoubleProperty>) token ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedBySpecificationEquivalence( ) throws Exception {
        // Setup
        final Method specification = BySpecificationOnProperty.class.getMethod( "getValue" );
        final Method target = WithPropertyWithDefinedEquivalence.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPropertyWithDefinedEquivalence> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithPropertyWithDefinedEquivalence>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                    specification, target );
        // Exercise
        final Equivalence<WithPropertyWithDefinedEquivalence> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create(
                eq( "getValue" ),
                EquivalenceAnnotationProcessorTest.<WithPropertyWithDefinedEquivalence, WithPublicProperty>functionOf( target ),
                (Equivalence<WithPublicProperty>) equivalenceCaptor.capture( ) );

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
                new EquivalenceAnnotationProcessor<WithObjectProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, annotationTypeReader,
                    specification, target );
        // Exercise
        final Equivalence<WithObjectProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory )
            .create( eq( "getValue" ), functionOf( target ), any( ObjectEqualityEquivalence.class ) );

        assertThat( equivalence, is( sameInstance( (Equivalence<WithObjectProperty>) token ) ) );
    }

    @Test
    public void failsWithUnknownAnnotationType( ) throws Exception {
        // Setup
        final AnnotationTypeReader mockAnnotationTypeReader = mock( AnnotationTypeReader.class );
        final Method specification = EqualityOnString.class.getMethod( "getValue" );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithPublicProperty>(
                    liftedEquivalenceFactory, equivalenceFactory, mockAnnotationTypeReader,
                        specification, target );

        doReturn( Override.class ).when( mockAnnotationTypeReader ).getEquivalenceAnnotationType( specification );
        // Exercise
        try
        {
            annotationProcessor.processEquivalenceSpecification( );
            // Verify
            fail( );
        }
        catch ( final IllegalStateException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "Cannot process annotation of type Override." ) ) );
        }
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
