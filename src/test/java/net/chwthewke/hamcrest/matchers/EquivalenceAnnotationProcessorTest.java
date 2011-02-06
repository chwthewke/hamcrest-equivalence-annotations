package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.LiftedEquivalence;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.specs.EqualityOnString;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.base.Function;

public class EquivalenceAnnotationProcessorTest {
    @Mock
    private LiftedEquivalenceFactory liftedEquivalenceFactory;

    @Mock
    private LiftedEquivalence<?, ?> token;

    @SuppressWarnings( "unchecked" )
    @Before
    public void setupLiftedEquivalenceFactory( ) {
        initMocks( this );
        when( liftedEquivalenceFactory.create( anyString( ), any( Function.class ), any( Equivalence.class ) ) )
            .thenReturn( token );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void liftedEqualityEquivalence( ) throws Exception {
        // Setup
        final Method specification = EqualityOnString.class.getMethod( "getValue" );
        final Method target = WithPublicProperty.class.getMethod( "getValue" );
        final EquivalenceAnnotationProcessor<WithPublicProperty> annotationProcessor =
                new EquivalenceAnnotationProcessor<WithPublicProperty>( liftedEquivalenceFactory, specification, target );
        // Exercise
        final Equivalence<WithPublicProperty> equivalence = annotationProcessor.processEquivalenceSpecification( );
        // Verify
        verify( liftedEquivalenceFactory ).create(
            eq( "getValue" ), functionOf( target ), any( EqualityEquivalence.class ) );
        assertThat( equivalence, is( sameInstance( (Equivalence<WithPublicProperty>) token ) ) );
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
