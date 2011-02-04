package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.NotPublic;

public interface AnnotationTestCases {

    @Identity
    Object singleAnnotation( );

    @Identity
    @NotPublic
    Object withAuxiliaryAnnotation( );

    @BySpecification( AnnotationTestCases.class )
    @ApproximateEquality( tolerance = 0.01d )
    Object tooManyAnnotations( );

    Object noAnnotationDefaultsToEquality( );
}
