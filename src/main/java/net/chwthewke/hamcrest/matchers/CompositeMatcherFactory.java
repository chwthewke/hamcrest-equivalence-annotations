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
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.NotPublic;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

class CompositeMatcherFactory<T> implements Equivalence<T> {

    static <T> Equivalence<T> asSpecifiedBy(
            final Class<?> matcherSpecification,
            final Class<T> matchedClass ) {

        return new CompositeMatcherFactory<T>(
                propertyFinderInstance,
                specificationValidatorInstance,
                matchedClass,
                matcherSpecification );
    }

    @SuppressWarnings( "unchecked" )
    static <T> Equivalence<T> asSpecifiedBy(
            final Class<? extends EquivalenceSpecification<T>> matcherSpecification ) {

        specificationValidatorInstance.validateSpecificationInterface( matcherSpecification );

        return asSpecifiedBy( matcherSpecification,
            (Class<T>) matcherSpecification.getAnnotation( EquivalenceSpecificationOn.class ).value( ) );
    }

    CompositeMatcherFactory(
            final PropertyFinder propertyFinder,
            final EquivalenceSpecificationValidator specificationValidator,
            final Class<T> matchedClass,
            final Class<?> matcherSpecification ) {
        this.propertyFinder = propertyFinder;
        this.specificationValidator = specificationValidator;
        this.matchedClass = checkNotNull( matchedClass );
        this.matcherSpecification = checkNotNull( matcherSpecification );

        specificationValidator.validateSpecificationInterface( matcherSpecification );

        initialize( );
    }

    public Matcher<T> equivalentTo( final T expected ) {
        return new CompositeMatcher<T>( matchedClass, propertyEquivalences, expected );
    }

    private void initialize( ) {
        checkSpecificationTargetClass( );

        addExpectedPropertyTemplates( );

        sortExpectedPropertyTemplates( );
    }

    private void sortExpectedPropertyTemplates( ) {
        final Comparator<PropertyEquivalence<T, ?>> comparator =
                Ordering.<String>natural( ).onResultOf(
                    new Function<PropertyEquivalence<T, ?>, String>( ) {
                        public String apply( final PropertyEquivalence<T, ?> expectedPropertyTemplate ) {
                            return expectedPropertyTemplate.getPropertyName( );
                        }
                    } );

        Collections.sort( propertyEquivalences, comparator );
    }

    private void addExpectedPropertyTemplates( ) {
        for ( final Method method : matcherSpecification.getMethods( ) )
            addExpectedPropertyTemplate( method );
    }

    private void addExpectedPropertyTemplate( final Method specificationMethod ) {

        final Method property = findMatchingProperty( specificationMethod );

        final PropertyEquivalence<T, ?> propertyEquivalence =
                new PropertyEquivalenceProvider<T>( propertyFinder,
                        specificationValidator,
                        property,
                        specificationMethod )
                    .get();
        propertyEquivalences.add( propertyEquivalence );
    }

    private Method findMatchingProperty( final Method specificationMethod ) {
        try
        {
            return propertyFinder.findProperty( matchedClass,
                    specificationMethod.getReturnType( ),
                    specificationMethod.getName( ),
                    specificationMethod.isAnnotationPresent( NotPublic.class ) );
        }
        catch ( final IllegalArgumentException e )
        {
            throw new IllegalArgumentException(
                String.format( "Error while binding specification method [%s]: %s",
                    specificationMethod, e.getMessage( ) ),
                e.getCause( ) );
        }
    }

    private void checkSpecificationTargetClass( ) {
        final Class<?> value =
                matcherSpecification.getAnnotation( EquivalenceSpecificationOn.class ).value( );
        if ( value != matchedClass )
            throw new IllegalArgumentException(
                String.format( "The %s annotation on %s must have a value of %s.",
                    EquivalenceSpecificationOn.class.getSimpleName( ),
                    matcherSpecification.getName( ),
                    matchedClass.getName( ) ) );
    }

    private final PropertyFinder propertyFinder;
    private final EquivalenceSpecificationValidator specificationValidator;

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final List<PropertyEquivalence<T, ?>> propertyEquivalences = newArrayList( );

    private static final PropertyFinder propertyFinderInstance = new PropertyFinder( );
    private static final EquivalenceSpecificationValidator specificationValidatorInstance = new EquivalenceSpecificationValidator( );
}
