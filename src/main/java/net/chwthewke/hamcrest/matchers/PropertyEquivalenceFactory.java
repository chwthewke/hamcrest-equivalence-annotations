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

import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.primitives.Primitives;
import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

class PropertyEquivalenceFactory<T> {

    public PropertyEquivalence<T, ?> getPropertyEquivalence() {
        final Class<?> originalPropertyType = property.getReturnType( );
        final Class<?> propertyType = Primitives.wrap( originalPropertyType );

        if ( specificationMethod.isAnnotationPresent( BySpecification.class ) )
            return getBySpecificationTemplate( propertyType,
                specificationMethod.getAnnotation( BySpecification.class ).value( ) );

        if ( specificationMethod.isAnnotationPresent( Equality.class ) )
            return getEqualityTemplate( propertyType );

        if ( specificationMethod.isAnnotationPresent( Identity.class ) )
        {
            if ( originalPropertyType.isPrimitive( ) )
                return getEqualityTemplate( propertyType );
            return getIdentityTemplate( propertyType );
        }

        if ( specificationMethod.isAnnotationPresent( ApproximateEquality.class ) )
            return getApproximateEqualityTemplate( propertyType,
                specificationMethod.getAnnotation( ApproximateEquality.class ).tolerance( ) );

        return getEqualityTemplate( propertyType );
    }

    PropertyEquivalenceFactory(
            final PropertyFinder propertyFinder,
            final EquivalenceSpecificationValidator specificationValidator,
            final Method property,
            final Method specificationMethod ) {
        this.propertyFinder = propertyFinder;
        this.specificationValidator = specificationValidator;
        this.property = property;
        this.specificationMethod = specificationMethod;
    }

    private PropertyEquivalence<T, Double> getApproximateEqualityTemplate( final Class<?> propertyType,
            final double tolerance ) {
        checkState( propertyType == Double.class || propertyType == Float.class );

        final Function<Double, Matcher<? super Double>> closeToMatcherFactory =
                new Function<Double, Matcher<? super Double>>( ) {
                    public Matcher<? super Double> apply( final Double expected ) {
                        return Matchers.closeTo( expected, tolerance );
                    }
                };

        // This should be the safest way to extract the property to a Double.
        final Function<T, Double> propertyFunction =
                Functions.compose( new Function<Number, Double>( ) {
                    public Double apply( final Number number ) {
                        return number == null ? null : number.doubleValue( );
                    }
                }, propertyFunction( Number.class ) );

        return PropertyEquivalence.create(
                property.getName(),
                propertyFunction,
                closeToMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getIdentityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> sameInstanceMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.sameInstance( expected );
                    }
                };

        return PropertyEquivalence.create(
                property.getName(),
                propertyFunction( propertyType ),
                sameInstanceMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getEqualityTemplate( final Class<U> propertyType ) {
        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return PropertyEquivalence.create(
                property.getName(),
                propertyFunction( propertyType ),
                equalToMatcherFactory );
    }

    private <U> PropertyEquivalence<T, U> getBySpecificationTemplate( final Class<U> propertyType,
            final Class<?> propertySpecification ) {

        final CompositeMatcherFactory<U> matcherFactoryForProperty =
                new CompositeMatcherFactory<U>( propertyFinder,
                        specificationValidator,
                        propertyType, propertySpecification );

        final Function<U, Matcher<? super U>> matcherBySpecificationFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return matcherFactoryForProperty.equivalentTo( expected );
                    }
                };
        return PropertyEquivalence.create(
                property.getName(),
                propertyFunction( propertyType ),
                matcherBySpecificationFactory );
    }

    private <U> Function<T, U> propertyFunction( final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( propertyType, item );
            }
        };
    }

    private <U> U extractProperty( final Class<U> propertyType, final T item ) {

        final boolean wasAccessible = property.isAccessible( );
        try
        {
            property.setAccessible( true );
            final Object rawProperty = property.invoke( item );
            try
            {
                return propertyType.cast( rawProperty );
            }
            catch ( final ClassCastException e )
            {
                throw new RuntimeException(
                    String.format( "Cannot cast result of property '%s()' on instance of %s to %s, actual type is %s.",
                        property.getName( ), item.getClass( ).getName( ),
                        propertyType.getName( ), rawProperty.getClass( ).getName( ) ),
                        e );
            }
        }
        catch ( final IllegalAccessException e )
        {
            throw new IllegalStateException( "Unpredicted illegal access", e );
        }
        catch ( final InvocationTargetException e )
        {
            throw new RuntimeException(
                String.format( "Exception while reading property %s on instance of %s.",
                    property.getName( ), item.getClass( ).getName( ) ),
                    e );
        }
        finally
        {
            property.setAccessible( wasAccessible );
        }
    }

    private final PropertyFinder propertyFinder;
    private final EquivalenceSpecificationValidator specificationValidator;

    private final Method property;
    private final Method specificationMethod;

}
