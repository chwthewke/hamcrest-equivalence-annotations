package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.google.common.base.Function;

public class LiftedEquivalenceTest {
    @Test
    public void equivalenceSucceedsOnProjectedValue( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", new ApproximateEqualityEquivalence(
                    5d ), GET_INT_VALUE );
        // Exercise
        // Verify
        assertThat( liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) )
            .matches( new WithPublicProperty( "124" ) ), is( true ) );
    }

    @Test
    public void equivalenceDescribesWithProjection( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", new ApproximateEqualityEquivalence(
                    5d ), GET_INT_VALUE );
        // Exercise
        final String description =
                describe( liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) ) );
        // Verify
        assertThat( description,
            is( equalTo( "getIntValue()=a numeric value within <5.0> of <123.0>" ) ) );
    }

    @Test
    public void equivalenceFailsOnProjectedValue( ) throws Exception {
        final Equivalence<WithPublicProperty> liftedEquivalence =
                new LiftedEquivalence<WithPublicProperty, Integer>( "getIntValue", new ApproximateEqualityEquivalence(
                    5d ), GET_INT_VALUE );
        final Matcher<WithPublicProperty> equivalentTo = liftedEquivalence.equivalentTo( new WithPublicProperty( "123" ) );
        final WithPublicProperty actual = new WithPublicProperty( "129" );
        // Exercise
        final boolean match = equivalentTo.matches( actual );
        final String mismatchDescription = describeMismatch( equivalentTo, actual );
        // Verify
        assertThat( match, is( false ) );
        assertThat( mismatchDescription, is( equalTo( "getIntValue() <129.0> differed by <1.0>" ) ) );
    }

    private static final Function<WithPublicProperty, Integer> GET_INT_VALUE = new Function<WithPublicProperty, Integer>( ) {
        public Integer apply( final WithPublicProperty input ) {
            return input.getIntValue( );
        }
    };

}
