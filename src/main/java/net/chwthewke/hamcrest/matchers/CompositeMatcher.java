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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

class CompositeMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    public void describeTo( final Description description ) {
        description
            .appendText( "a " )
            .appendText( expectedType.getSimpleName( ) );

        String subMatcherLeadin = " with";

        for ( final Entry<ExpectedPropertyTemplate<T, ?>, Matcher<?>> entry : expectedProperties.entrySet( ) )
        {
            description
                .appendText( subMatcherLeadin )
                .appendText( " " )
                .appendText( entry.getKey( ).getPropertyName( ) )
                .appendText( "()=" )
                .appendDescriptionOf( entry.getValue( ) );
            subMatcherLeadin = ",";
        }

    }

    @Override
    protected boolean matchesSafely( final T item, final Description mismatchDescription ) {

        for ( final Entry<ExpectedPropertyTemplate<T, ?>, Matcher<?>> entry : expectedProperties.entrySet( ) )
        {
            final Matcher<?> matcher = entry.getValue( );
            final ExpectedPropertyTemplate<T, ?> matcherTemplate = entry.getKey( );

            final Object propertyValue = matcherTemplate.extractPropertyValue( item );
            if ( !matcher.matches( propertyValue ) )
            {
                mismatchDescription
                    .appendText( matcherTemplate.getPropertyName( ) )
                    .appendText( "() " );
                matcher.describeMismatch( propertyValue, mismatchDescription );

                return false;
            }
        }

        return true;
    }

    CompositeMatcher( final Class<T> expectedType,
            final Collection<ExpectedPropertyTemplate<T, ?>> expectedPropertyTemplates,
            final T expected ) {

        super( expectedType );

        this.expectedType = checkNotNull( expectedType );

        for ( final ExpectedPropertyTemplate<T, ?> expectedPropertyTemplate : expectedPropertyTemplates )
            expectedProperties.put( expectedPropertyTemplate, expectedPropertyTemplate.specializeFor( expected ) );
    }

    private final Class<T> expectedType;
    private final Map<ExpectedPropertyTemplate<T, ?>, Matcher<?>> expectedProperties = newLinkedHashMap( );

}
