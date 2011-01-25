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

import org.hamcrest.Matcher;

import com.google.common.base.Function;

final class PropertyEquivalence<T, U> {

    static <T, U> PropertyEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Function<U, Matcher<? super U>> matcherFactory ) {
        return new PropertyEquivalence<T, U>( propertyName, propertyMethod, matcherFactory );
    }

    private PropertyEquivalence( final String propertyName,
                                 final Function<T, U> propertyMethod,
                                 final Function<U, Matcher<? super U>> matcherFactory ) {
        this.propertyName = propertyName;
        this.propertyMethod = propertyMethod;
        this.matcherFactory = matcherFactory;
    }

    public String getPropertyName( ) {
        return propertyName;
    }

    public U extractPropertyValue( final T item ) {
        return propertyMethod.apply( item );
    }

    public Matcher<? super U> specializeFor( final T expected ) {
        return matcherFactory.apply( propertyMethod.apply( expected ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Function<U, Matcher<? super U>> matcherFactory;

}
