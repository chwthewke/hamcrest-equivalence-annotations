package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.junit.Test;

public class CompositeMatcherFactoryTest {

    @Test
    public void equivalenceSpecificationAnnotationMismatch() throws Exception {
        // Setup

        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithAnnotationMismatch.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The EquivalenceSpecificationOn annotation on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithAnnotationMismatch must " +
                        "have a value of net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationTargetsMissingMethod() throws Exception {
        // Setup
        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithMisnamedMethod.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Error while binding specification method " +
                        "[public abstract java.lang.String net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithMisnamedMethod.value()]: " +
                        "The matched class net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched" +
                        " lacks the public property 'value()'." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationTargetsMethodWithArguments() throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithArgumentsMismatch.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Error while binding specification method " +
                        "[public abstract java.lang.String net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithArgumentsMismatch.compute()]: " +
                        "The matched class net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched " +
                        "lacks the public property 'compute()'." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationHasIncompatibleReturnType() throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithReturnTypeMismatch.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Error while binding specification method " +
                        "[public abstract java.lang.Integer net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithReturnTypeMismatch.getValue()]: " +
                        "The property 'getValue()' on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched " +
                        "has return type java.lang.String which is not assignable to java.lang.Integer." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationTargetsPrivateProperty() throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithPrivateProperty.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Error while binding specification method " +
                        "[public abstract int net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithPrivateProperty.getId()]: " +
                        "The matched class net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched lacks the public property 'getId()'." ) ) );

        }
    }

    @Test
    public void expectedPropertyThrowsException( ) throws Exception {
        // Setup
        final Equivalence<Matched> factory = asSpecifiedBy( ExpectedPropertyThrows.class );
        // Exercise
        try
        {
            factory.equivalentTo( new Matched( "" ) );
            // Verify
            fail( );
        }
        catch ( final RuntimeException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Exception while reading property getException on instance of " +
                        "net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched." ) ) );
        }

    }

    public static class Matched {

        public Matched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        public String compute( final String foo ) {
            return value + foo;
        }

        public void run( ) {
        }

        public Object getException( ) {
            throw new RuntimeException( );
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @EquivalenceSpecificationOn( Object.class )
    public static interface SpecificationWithAnnotationMismatch {
        @Equality
        String getValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithMisnamedMethod {
        @Equality
        String value( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithArgumentsMismatch {
        @Equality
        String compute( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithReturnTypeMismatch {
        @Equality
        Integer getValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithPrivateProperty {
        @Equality
        int getId( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface ExpectedPropertyThrows extends EquivalenceSpecification<Matched> {
        @Equality
        Object getException( );
    }

}
