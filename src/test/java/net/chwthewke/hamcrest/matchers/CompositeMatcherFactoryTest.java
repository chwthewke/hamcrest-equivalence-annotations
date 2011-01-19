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

import static net.chwthewke.hamcrest.matchers.EquivalenceMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

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
