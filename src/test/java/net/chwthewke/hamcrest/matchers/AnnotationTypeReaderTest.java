package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
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
        final Class<? extends Annotation> annotationType =
                annotationTypeReader.getEquivalenceAnnotationType( AnnotationTestCases.class.getMethod( "singleAnnotation" ) );
        // Verify
        assertThat( "", annotationType == Identity.class );
    }

    @Test
    public void singleAnnotationWithAuxiliaryIsSelected( ) throws Exception {
        // Setup
        // Exercise
        final Class<? extends Annotation> annotationType =
                annotationTypeReader.getEquivalenceAnnotationType( AnnotationTestCases.class.getMethod( "withAuxiliaryAnnotation" ) );
        // Verify
        assertThat( "", annotationType == Identity.class );
    }

    @Test
    public void tooManyAnnotationsCauseException( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            annotationTypeReader.getEquivalenceAnnotationType( AnnotationTestCases.class.getMethod( "tooManyAnnotations" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
        }
    }

    @Test
    public void noAnnotationDefaultsToEquality( ) throws Exception {
        // Setup

        // Exercise
        final Class<? extends Annotation> annotationType =
                annotationTypeReader.getEquivalenceAnnotationType( AnnotationTestCases.class.getMethod( "noAnnotationDefaultsToEquality" ) );
        // Verify
        assertThat( "", annotationType == Equality.class );
    }

}
