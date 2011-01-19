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

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;

import java.lang.reflect.Method;

class FindVisiblePropertyFunction extends FindPropertyFunction {

    FindVisiblePropertyFunction( ) {
    }

    @Override
    protected Method getPropertyMethod( final Class<?> clazz, final String propertyName ) {
        try
        {
            final Method method = getAnyPropertyMethod( clazz, propertyName );

            if ( !isVisible( method.getDeclaringClass( ), clazz, method.getModifiers( ) ) )
                return raisePropertyNotFound( null, clazz, propertyName, VISIBLE_QUALIFIER );

            return method;
        }
        catch ( final NoSuchMethodException e )
        {
            return raisePropertyNotFound( e, clazz, propertyName, VISIBLE_QUALIFIER );
        }
    }

    private Method getAnyPropertyMethod( final Class<?> clazz, final String propertyName ) throws NoSuchMethodException {
        try
        {
            return clazz.getDeclaredMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            final Class<?> superClazz = clazz.getSuperclass( );
            if ( superClazz == null )
                throw e;
            return getAnyPropertyMethod( superClazz, propertyName );
        }
    }

    private boolean isVisible( final Class<?> declarationSite, final Class<?> callSite, final int modifiers ) {
        if ( isPublic( modifiers ) || isProtected( modifiers ) )
            return true;

        if ( isPrivate( modifiers ) )
            return declarationSite.equals( callSite );

        return declarationSite.getPackage( ).equals( callSite.getPackage( ) );
    }

    private static final String VISIBLE_QUALIFIER = "visible";
}
