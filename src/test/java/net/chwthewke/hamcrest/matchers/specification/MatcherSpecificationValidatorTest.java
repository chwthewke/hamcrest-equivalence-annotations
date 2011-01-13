package net.chwthewke.hamcrest.matchers.specification;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.MatcherOf;
import net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest.Matched;

import org.junit.Before;
import org.junit.Test;

public class MatcherSpecificationValidatorTest {

    @Before
    public void setupValidator( ) {
        specificationValidator = new MatcherSpecificationValidator( );
    }

    @Test
    public void matchingSpecificationNotAnInterface( ) throws Exception {
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
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers.specification." +
                        "MatcherSpecificationValidatorTest$SpecificationNotAnInterface must be an interface." ) ) );
        }
    }

    @Test
    public void matchingSpecificationNonPublicInterface( ) throws Exception {
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
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers.specification." +
                        "MatcherSpecificationValidatorTest$NonPublicInterface must have public visibility." ) ) );
        }
    }

    @Test
    public void matchingSpecificationMissingAnnotation( ) throws Exception {
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
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers.specification." +
                        "MatcherSpecificationValidatorTest$SpecificationWithoutAnnotation must be " +
                        "annotated with MatcherOf." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasVoidMethod( ) throws Exception {
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
                        + "hamcrest.matchers.specification.MatcherSpecificationValidatorTest"
                        + "$SpecificationWithVoidMethod.run() in specification "
                        + "net.chwthewke.hamcrest.matchers.specification.MatcherSpecificationValidatorTest"
                        + "$SpecificationWithVoidMethod has return type void." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasNonPropertyMethod( ) throws Exception {
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
                        "net.chwthewke.hamcrest.matchers.specification.MatcherSpecificationValidatorTest" +
                        "$SpecificationWithNonPropertyMethod.method(java.lang.Object) " +
                        "in specification net.chwthewke.hamcrest.matchers.specification." +
                        "MatcherSpecificationValidatorTest$SpecificationWithNonPropertyMethod " +
                        "has parameters." ) ) );
        }
    }

    private MatcherSpecificationValidator specificationValidator;

    @MatcherOf( Matched.class )
    public static class SpecificationNotAnInterface {
    }

    @MatcherOf( Matched.class )
    static interface NonPublicInterface {
        @Equality
        String getValue( );
    }

    public static interface SpecificationWithoutAnnotation {
        @Equality
        String getValue( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithNonPropertyMethod {
        @Equality
        String method( Object input );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithVoidMethod {
        @Equality
        void run( );
    }
}
