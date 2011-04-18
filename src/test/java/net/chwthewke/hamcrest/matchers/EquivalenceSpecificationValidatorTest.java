package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.matchers.CompositeEquivalenceTest.Matched;

import org.junit.Before;
import org.junit.Test;

public class EquivalenceSpecificationValidatorTest {

    @Before
    public void setupValidator( ) {
        specificationValidator = new EquivalenceSpecificationValidator( );
    }

    @Test
    public void equivalenceSpecificationNotAnInterface( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface(
                SpecificationNotAnInterface.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$SpecificationNotAnInterface must be an interface." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationNonPublicInterface( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface(
                NonPublicInterface.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$NonPublicInterface must have public visibility." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationMissingAnnotation( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface(
                SpecificationWithoutAnnotation.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$SpecificationWithoutAnnotation must be " +
                        "annotated with EquivalenceSpecificationOn." ) ) );
        }
    }

    @Test
    public void equivalenceSpecificationIsEmpty( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface( EmptySpecification.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "The 'equivalenceSpecification' " +
                    "net.chwthewke.hamcrest.matchers." +
                    "EquivalenceSpecificationValidatorTest$EmptySpecification " +
                    "must have at least one method." ) ) );
        }
    }

    @Test
    public void equivalenceInterfaceHasVoidMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface(
                SpecificationWithVoidMethod.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The method public abstract void net.chwthewke."
                        + "hamcrest.matchers.EquivalenceSpecificationValidatorTest"
                        + "$SpecificationWithVoidMethod.run() in specification "
                        + "net.chwthewke.hamcrest.matchers.EquivalenceSpecificationValidatorTest"
                        + "$SpecificationWithVoidMethod has return type void." ) ) );
        }
    }

    @Test
    public void equivalenceInterfaceHasNonPropertyMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            specificationValidator.validateSpecificationInterface(
                SpecificationWithNonPropertyMethod.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The method public abstract java.lang.String " +
                        "net.chwthewke.hamcrest.matchers.EquivalenceSpecificationValidatorTest" +
                        "$SpecificationWithNonPropertyMethod.method(java.lang.Object) " +
                        "in specification net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$SpecificationWithNonPropertyMethod " +
                        "has parameters." ) ) );
        }
    }

    private EquivalenceSpecificationValidator specificationValidator;

    @EquivalenceSpecificationOn( Matched.class )
    public static class SpecificationNotAnInterface {
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface EmptySpecification {
    }

    @EquivalenceSpecificationOn( Matched.class )
    static interface NonPublicInterface {
        @Equality
        String getValue( );
    }

    public static interface SpecificationWithoutAnnotation {
        @Equality
        String getValue( );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithNonPropertyMethod {
        @Equality
        String method( Object input );
    }

    @EquivalenceSpecificationOn( Matched.class )
    public static interface SpecificationWithVoidMethod {
        @Equality
        void run( );
    }
}
