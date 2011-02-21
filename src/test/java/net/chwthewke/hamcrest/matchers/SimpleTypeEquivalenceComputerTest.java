package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IdentityEquivalence;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeEquivalenceComputerTest {

    private SimpleTypeEquivalenceComputer typeEquivalenceComputer;
    private EquivalenceFactory equivalenceFactory;

    @Before
    public void setupTypeEquivalenceComputer( ) {
        equivalenceFactory = mock( EquivalenceFactory.class );
        typeEquivalenceComputer = new SimpleTypeEquivalenceComputer( equivalenceFactory );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeEqualityEquivalenceOnString( ) throws Exception {
        // Setup
        final Annotation equality = equality( );

        final Equivalence<String> equivalence = mock( Equivalence.class );

        when( equivalenceFactory.<String>getEquality( ) ).thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( equality, String.class );
        // Verify
        verify( equivalenceFactory ).getEquality( );
        assertThat( (Class<String>) typeEquivalence.getType( ), is( equalTo( String.class ) ) );
        assertThat( (Equivalence<String>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeIdentityOnObject( ) throws Exception {
        // Setup
        final Annotation identity = identity( );

        final Equivalence<Object> equivalence = mock( Equivalence.class );
        when( equivalenceFactory.getIdentity( ) ).thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( identity, Object.class );

        // Verify
        verify( equivalenceFactory ).getIdentity( );
        assertThat( (Class<Object>) typeEquivalence.getType( ), is( equalTo( Object.class ) ) );
        assertThat( (Equivalence<Object>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );

    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeApproximateEqualityOnPrimitiveFloat( ) throws Exception {
        // Setup
        final Annotation approx = approximateEquality( 0.001d );

        final Equivalence<Number> equivalence = mock( Equivalence.class );
        when( equivalenceFactory.getApproximateEquality( 0.001d ) )
            .thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( approx, float.class );

        // Verify
        verify( equivalenceFactory ).getApproximateEquality( 0.001d );
        assertThat( (Class<Number>) typeEquivalence.getType( ), is( equalTo( Number.class ) ) );
        assertThat( (Equivalence<Number>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );

    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeApproximateEqualityOnBoxedDouble( ) throws Exception {
        // Setup
        final Annotation approx = approximateEquality( 2e-6d );

        final Equivalence<Number> equivalence = mock( Equivalence.class );
        when( equivalenceFactory.getApproximateEquality( 2e-6d ) )
            .thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( approx, Double.class );

        // Verify
        verify( equivalenceFactory ).getApproximateEquality( 2e-6d );
        assertThat( (Class<Number>) typeEquivalence.getType( ), is( equalTo( Number.class ) ) );
        assertThat( (Equivalence<Number>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );

    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeBySpecificationEquivalence( ) throws Exception {
        // Setup
        final BySpecification bySpecification = bySpecification( EquivalenceSpecification.class );

        final Equivalence<Date> equivalence = mock( Equivalence.class );
        when( equivalenceFactory.<Date>getEquivalenceBySpecification( bySpecification, Date.class ) )
            .thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( bySpecification, Date.class );

        // Verify
        verify( equivalenceFactory ).getEquivalenceBySpecification( bySpecification, Date.class );
        assertThat( (Class<Date>) typeEquivalence.getType( ), is( equalTo( Date.class ) ) );
        assertThat( (Equivalence<Date>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );
    }

    @SuppressWarnings( "unchecked" )
    @Test
    public void computeByEquivalenceEquivalence( ) throws Exception {
        // Setup
        final ByEquivalence byEquivalence = byEquivalence( (Class<? extends Equivalence<?>>) IdentityEquivalence.class );

        final Equivalence<Calendar> equivalence = mock( Equivalence.class );
        when( equivalenceFactory.<Calendar>createEquivalenceInstance( byEquivalence, Calendar.class ) )
            .thenReturn( equivalence );
        // Exercise
        final TypeEquivalence<?> typeEquivalence =
                typeEquivalenceComputer.computeEquivalenceOnPropertyType( byEquivalence, Calendar.class );

        // Verify
        verify( equivalenceFactory ).createEquivalenceInstance( byEquivalence, Calendar.class );
        assertThat( (Class<Calendar>) typeEquivalence.getType( ), is( equalTo( Calendar.class ) ) );
        assertThat( (Equivalence<Calendar>) typeEquivalence.getEquivalence( ), is( sameInstance( equivalence ) ) );
    }

    private Equality equality( ) {
        return new Equality( ) {
            public Class<? extends Annotation> annotationType( ) {
                return Equality.class;
            }
        };
    }

    private Identity identity( ) {
        return new Identity( ) {
            public Class<? extends Annotation> annotationType( ) {
                return Identity.class;
            }
        };
    }

    private ApproximateEquality approximateEquality( final double tolerance ) {
        return new ApproximateEquality( ) {
            public Class<? extends Annotation> annotationType( ) {
                return ApproximateEquality.class;
            }

            public double tolerance( ) {
                return tolerance;
            }
        };
    }

    private BySpecification bySpecification( final Class<?> specification ) {
        return new BySpecification( ) {

            public Class<? extends Annotation> annotationType( ) {
                return BySpecification.class;
            }

            public Class<?> value( ) {
                return specification;
            }
        };
    }

    private ByEquivalence byEquivalence( final Class<? extends Equivalence<?>> value ) {
        return new ByEquivalence( ) {

            public Class<? extends Annotation> annotationType( ) {
                return ByEquivalence.class;
            }

            public Class<? extends Equivalence<?>> value( ) {
                return value;
            }
        };
    }

}
