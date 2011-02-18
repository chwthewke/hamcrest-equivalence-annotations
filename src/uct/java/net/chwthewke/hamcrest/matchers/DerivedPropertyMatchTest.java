package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.Equivalences.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

import org.hamcrest.Matcher;
import org.junit.Test;

public class DerivedPropertyMatchTest {

    @Test
    public void equivalenceOnDerivedProperty() throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnDerivedProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        // Exercise
        final boolean match = matcher.matches( new DerivedMatched( "123" ) );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void nonEquivalenceOnDerivedProperty() throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnDerivedProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        // Exercise
        final boolean match = matcher.matches( new DerivedMatched( "234" ) );
        // Verify
        assertThat( match, is( false ) );
    }

    @Test
    public void equivalenceOnOverridenProperty() throws Exception {
        // Setup
        final Matcher<DerivedMatched> matcher =
                asSpecifiedBy( SpecificationOnOverridenProperty.class )
                    .equivalentTo( new DerivedMatched( "123" ) );
        final DerivedMatched actual = new DerivedMatched( "0" );
        actual.setValue( "123" );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    public void equivalenceOnBasePropertyUsesBasePropertyOnOverridenType() throws Exception {
        // Setup
        final Matcher<Matched> matcher = asSpecifiedBy( SpecificationOnBaseProperty.class )
            .equivalentTo( new Matched( "123" ) );
        final DerivedMatched actual = new DerivedMatched( "0" );
        actual.setValue( "123" );
        // Exercise
        final boolean match = matcher.matches( actual );
        // Verify
        assertThat( match, is( true ) );
    }

    public static class DerivedMatched extends Matched {

        public DerivedMatched( final String value ) {
            super( value );
            value0 = value;
        }

        @Override
        public String getValue( ) {
            return value0;
        }

        public void setValue( final String value ) {
            value0 = value;
        }

        private String value0;
    }

    public static class Matched {

        public Matched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        public int getIntValue( ) {
            try
            {
                return Integer.parseInt( value );
            }
            catch ( final NumberFormatException e )
            {
                return Integer.MIN_VALUE;
            }
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @EquivalenceSpecificationOn( DerivedMatched.class )
    public static interface SpecificationOnDerivedProperty extends EquivalenceSpecification<DerivedMatched> {
        @Identity
        int getIntValue( );
    }

    @EquivalenceSpecificationOn( DerivedMatched.class )
    public static interface SpecificationOnOverridenProperty extends EquivalenceSpecification<DerivedMatched> {
        @Equality
        String getValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationOnBaseProperty extends EquivalenceSpecification<Matched> {
        @Equality
        String getValue( );
    }

}
