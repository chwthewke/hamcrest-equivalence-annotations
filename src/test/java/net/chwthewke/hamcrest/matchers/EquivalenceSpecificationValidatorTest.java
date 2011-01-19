/*
 * Copyright (c) 2010-2011, Thomas Dufour
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.chwthewke.hamcrest.matchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest.Matched;

import org.junit.Before;
import org.junit.Test;

public class EquivalenceSpecificationValidatorTest {

    @Before
    public void setupValidator( ) {
        specificationValidator = new EquivalenceSpecificationValidator( );
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
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$SpecificationNotAnInterface must be an interface." ) ) );
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
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$NonPublicInterface must have public visibility." ) ) );
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
                is( equalTo( "The 'equivalenceSpecification' net.chwthewke.hamcrest.matchers." +
                        "EquivalenceSpecificationValidatorTest$SpecificationWithoutAnnotation must be " +
                        "annotated with EquivalenceSpecificationOn." ) ) );
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
                        + "hamcrest.matchers.EquivalenceSpecificationValidatorTest"
                        + "$SpecificationWithVoidMethod.run() in specification "
                        + "net.chwthewke.hamcrest.matchers.EquivalenceSpecificationValidatorTest"
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
