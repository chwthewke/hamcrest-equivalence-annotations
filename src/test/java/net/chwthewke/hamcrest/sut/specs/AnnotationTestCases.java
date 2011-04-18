package net.chwthewke.hamcrest.sut.specs;

import java.util.Collection;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.NotPublic;
import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;

public interface AnnotationTestCases {

    @Identity
    Object singleAnnotation( );

    @Identity
    @NotPublic
    Object withAuxiliaryAnnotation( );

    @Identity
    @OnIterableElements( elementType = String.class )
    Collection<String> withContainerAnnotation( );

    @BySpecification( AnnotationTestCases.class )
    @ApproximateEquality( tolerance = 0.01d )
    Object tooManyAnnotations( );

    @OnIterableElements( elementType = Object.class )
    @OnArrayElements
    Object tooManyContainerAnnotations( );

    Object noAnnotationDefaultsToEquality( );
}
