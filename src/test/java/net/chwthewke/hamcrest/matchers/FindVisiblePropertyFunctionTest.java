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

import java.lang.reflect.Method;

import net.chwthewke.hamcrest.matchers.FindPropertyFunction;
import net.chwthewke.hamcrest.matchers.FindVisiblePropertyFunction;
import net.chwthewke.hamcrest.matchers.use_case_classes.DerivedFromPrivateProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.DerivedWithPackageLocalProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.DerivedWithProtectedProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.DerivedWithPublicProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.WithPackageLocalProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.WithPrivateProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.WithProtectedProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.WithPublicProperty;
import net.chwthewke.hamcrest.matchers.use_case_classes.isolated.IsolatedDerivedWithPackageLocalProperty;

import org.junit.Before;
import org.junit.Test;

public class FindVisiblePropertyFunctionTest {

    private FindPropertyFunction methodFinder;

    @Before
    public void setupMethodFinder( ) {
        methodFinder = new FindVisiblePropertyFunction( );
    }

    @Test
    public void findPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( WithPublicProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( WithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findOverridenPublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPublicProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getValue" ) ) );
    }

    @Test
    public void findBasePublicProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPublicProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( DerivedWithPublicProperty.class.getMethod( "getIntValue" ) ) );
    }

    @Test
    public void findProtectedProperty( ) throws Exception {
        // Setup

        // Exercise

        final Method method = methodFinder
            .findPropertyMethod( DerivedWithProtectedProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( DerivedWithProtectedProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void findBaseProtectedProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithProtectedProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( equalTo( WithProtectedProperty.class.getDeclaredMethod( "getIntValue" ) ) ) );

    }

    @Test
    public void findPackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( WithPackageLocalProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( WithPackageLocalProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void findOverridenPackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPackageLocalProperty.class, String.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( DerivedWithPackageLocalProperty.class.getDeclaredMethod( "getValue" ) ) ) );

    }

    @Test
    public void findBasePackageLocalProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( DerivedWithPackageLocalProperty.class, int.class, "getIntValue" );
        // Verify
        assertThat( method, is( equalTo( WithPackageLocalProperty.class.getDeclaredMethod( "getIntValue" ) ) ) );

    }

    @Test
    public void dontFindNonLocalBasePackageLocalProperty( ) throws Exception {
        // Setup

        try
        {
            // Exercise
            methodFinder
                .findPropertyMethod( IsolatedDerivedWithPackageLocalProperty.class, String.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.matchers.use_case_classes." +
                        "isolated.IsolatedDerivedWithPackageLocalProperty lacks the visible property 'getValue()'." ) ) );
        }
    }

    @Test
    public void findPrivateProperty( ) throws Exception {
        // Setup

        // Exercise
        final Method method = methodFinder
            .findPropertyMethod( WithPrivateProperty.class, long.class, "getValue" );
        // Verify
        assertThat( method, is( equalTo( WithPrivateProperty.class.getDeclaredMethod( "getValue" ) ) ) );
    }

    @Test
    public void dontFindPrivatePropertyInSuperclass( ) throws Exception {
        // Setup

        try
        {
            // Exercise
            methodFinder
                .findPropertyMethod( DerivedFromPrivateProperty.class, long.class, "getValue" );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ), is( equalTo( "The matched class net.chwthewke.hamcrest.matchers.use_case_classes." +
                        "DerivedFromPrivateProperty lacks the visible property 'getValue()'." ) ) );
        }
    }

}
