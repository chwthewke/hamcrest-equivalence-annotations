package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.sut.specs.AnnotationTestCases;

import org.junit.Before;
import org.junit.Test;

public class AnnotationTypeReaderTest {

    private AnnotationTypeReader annotationTypeReader;

    @Before
    public void setupAnnotationTypeReader( ) {
        annotationTypeReader = new AnnotationTypeReader( );
    }

    @Test
    public void singleAnnotationIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final Annotation equivalenceAnnotation =
                annotationTypeReader.getEquivalenceAnnotation( AnnotationTestCases.class.getMethod( "singleAnnotation" ) );
        // Verify
        assertThat( equivalenceAnnotation, is( instanceOf( Identity.class ) ) );
    }

    @Test
    public void singleAnnotationWithAuxiliaryIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final Annotation equivalenceAnnotation = annotationTypeReader.getEquivalenceAnnotation( AnnotationTestCases.class.getMethod( "withAuxiliaryAnnotation" ) );
        // Verify
        assertThat( equivalenceAnnotation, is( instanceOf( Identity.class ) ) );
    }

    @Test
    public void tooManyAnnotationsCauseException( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            annotationTypeReader.getEquivalenceAnnotation( AnnotationTestCases.class.getMethod( "tooManyAnnotations" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat( e.getMessage( ), is( equalTo( "The equivalence specification property " +
                    "public abstract java.lang.Object net.chwthewke.hamcrest.sut.specs." +
                    "AnnotationTestCases.tooManyAnnotations() has these mutually exclusive annotations: " +
                    "[ApproximateEquality, BySpecification]." ) ) );
        }
    }

    @Test
    public void noAnnotationDefaultsToEquality( ) throws Exception {
        // Setup

        // Exercise
        final Annotation equivalenceAnnotation = annotationTypeReader.getEquivalenceAnnotation( AnnotationTestCases.class.getMethod( "noAnnotationDefaultsToEquality" ) );
        // Verify
        assertThat( equivalenceAnnotation, is( instanceOf( Equality.class ) ) );
    }

}
