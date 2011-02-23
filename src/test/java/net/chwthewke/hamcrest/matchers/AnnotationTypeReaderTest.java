package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.sut.specs.AnnotationTestCases;

import org.junit.Before;
import org.junit.Test;

public class AnnotationTypeReaderTest {

    private AnnotationReader annotationReader;

    @Before
    public void setupAnnotationReader( ) {
        annotationReader = new AnnotationReader( );
    }

    @Test
    public void singleEquivalenceAnnotationIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final TypeEquivalenceSpecification<?> equivalenceSpecification =
                annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "singleAnnotation" ) );
        // Verify
        assertThat( equivalenceSpecification.getEquivalenceAnnotation( ), is( instanceOf( Identity.class ) ) );
        assertThat( equivalenceSpecification.hasContainerAnnotation( ), is( false ) );
    }

    @Test
    public void singleAnnotationWithAuxiliaryIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final TypeEquivalenceSpecification<?> equivalenceSpecification =
                annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "withAuxiliaryAnnotation" ) );
        // Verify
        assertThat( equivalenceSpecification.getEquivalenceAnnotation( ), is( instanceOf( Identity.class ) ) );
        assertThat( equivalenceSpecification.hasContainerAnnotation( ), is( false ) );
    }

    @Test
    public void singleAnnotationWithContainerAnnotationIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final TypeEquivalenceSpecification<?> equivalenceSpecification =
                annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "withContainerAnnotation" ) );
        // Verify
        assertThat( equivalenceSpecification.getEquivalenceAnnotation( ), is( instanceOf( Identity.class ) ) );
        assertThat( equivalenceSpecification.getContainerAnnotation( ), is( instanceOf( OnIterableElements.class ) ) );
    }

    @Test
    public void tooManyAnnotationsCauseException( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "tooManyAnnotations" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "The equivalence specification property " +
                    "tooManyAnnotations has these mutually exclusive annotations: " +
                    "[@net.chwthewke.hamcrest.annotations.ApproximateEquality(tolerance=0.01), " +
                    "@net.chwthewke.hamcrest.annotations.BySpecification(value=interface net.chwthewke." +
                    "hamcrest.sut.specs.AnnotationTestCases)]." ) ) );
        }
    }

    @Test
    public void tooManyContainerAnnotationsCauseException( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "tooManyContainerAnnotations" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The equivalence specification property " +
                        "tooManyContainerAnnotations has these mutually exclusive " +
                        "annotations: [@net.chwthewke.hamcrest.annotations.OnArrayElements(inOrder=true), " +
                        "@net.chwthewke.hamcrest.annotations.OnIterableElements(" +
                        "elementType=class java.lang.Object, inOrder=true)]." ) ) );
        }
    }

    @Test
    public void noAnnotationDefaultsToEquality( ) throws Exception {
        // Setup

        // Exercise
        final TypeEquivalenceSpecification<?> equivalenceAnnotation =
                annotationReader.getTypeEquivalenceSpecification( AnnotationTestCases.class.getMethod( "noAnnotationDefaultsToEquality" ) );
        // Verify
        assertThat( equivalenceAnnotation.getEquivalenceAnnotation( ), is( instanceOf( Equality.class ) ) );
        assertThat( equivalenceAnnotation.hasContainerAnnotation( ), is( false ) );
    }

}
