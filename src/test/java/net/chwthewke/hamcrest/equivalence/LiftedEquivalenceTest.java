package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import com.google.common.base.Function;

public class LiftedEquivalenceTest {
    @Test
    public void equivalenceSucceedsOnProjectedValue( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", GET_INT_VALUE, new ApproximateEqualityEquivalence( 5d ) );
        // Exercise
        // Verify
        assertThat( liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) )
            .matches( new WithPublicProperty( "124" ) ), is( true ) );
    }

    @Test
    public void equivalenceDescribesWithProjection( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", GET_INT_VALUE, new ApproximateEqualityEquivalence( 5d ) );
        final Description description = new StringDescription( );
        // Exercise
        liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) )
            .describeTo( description );
        // Verify
        assertThat( description.toString( ), is( equalTo( "getIntValue()=a numeric value within <5.0> of <123.0>" ) ) );
    }

    @Test
    public void equivalenceFailsOnProjectedValue( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", GET_INT_VALUE, new ApproximateEqualityEquivalence( 5d ) );
        final Matcher<WithPublicProperty> equivalentTo = liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) );
        final Description description = new StringDescription( );
        final WithPublicProperty actual = new WithPublicProperty( "129" );
        // Exercise
        final boolean match = equivalentTo.matches( actual );
        equivalentTo.describeMismatch( actual, description );
        // Verify
        assertThat( match, is( false ) );
        assertThat( description.toString( ), is( equalTo( "getIntValue() <129.0> differed by <1.0>" ) ) );
    }

    private static final Function<WithPublicProperty, Integer> GET_INT_VALUE = new Function<WithPublicProperty, Integer>( ) {
        public Integer apply( final WithPublicProperty input ) {
            return input.getIntValue( );
        }
    };

}
