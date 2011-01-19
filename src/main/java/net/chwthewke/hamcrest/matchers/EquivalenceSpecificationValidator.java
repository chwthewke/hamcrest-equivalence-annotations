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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;

class EquivalenceSpecificationValidator {

    public void validateSpecificationInterface( final Class<?> equivalenceSpecification ) {

        checkSpecificationIsAPublicInterface( equivalenceSpecification );

        checkMatcherOfAnnotation( equivalenceSpecification );

        for ( final Method method : equivalenceSpecification.getMethods( ) )
            checkValidSpecificationProperty( method );
    }

    private void checkSpecificationIsAPublicInterface( final Class<?> equivalenceSpecification ) {
        if ( !equivalenceSpecification.isInterface( ) )
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must be an interface.",
                    equivalenceSpecification.getName( ) ) );

        if ( !Modifier.isPublic( equivalenceSpecification.getModifiers( ) ) )
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must have public visibility.",
                    equivalenceSpecification.getName( ) ) );
    }

    private static void checkMatcherOfAnnotation( final Class<?> equivalenceSpecification ) {
        if ( !equivalenceSpecification.isAnnotationPresent( EquivalenceSpecificationOn.class ) )
        {
            throw new IllegalArgumentException(
                String.format( "The 'equivalenceSpecification' %s must be annotated with %s.",
                    equivalenceSpecification.getName( ),
                    EquivalenceSpecificationOn.class.getSimpleName( ) ) );
        }
    }

    private void checkValidSpecificationProperty( final Method method ) {
        if ( method.getReturnType( ) == void.class )
            throw new IllegalArgumentException(
                String.format( "The method %s in specification %s has return type void.",
                    method,
                    method.getDeclaringClass( ).getName( ) ) );

        if ( method.getParameterTypes( ).length != 0 )
            throw new IllegalArgumentException(
                String.format( "The method %s in specification %s has parameters.",
                    method,
                    method.getDeclaringClass( ).getName( ) ) );
    }

}
