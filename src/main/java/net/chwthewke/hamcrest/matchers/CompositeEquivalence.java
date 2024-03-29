package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static net.chwthewke.hamcrest.matchers.EquivalenceAnnotationProcessor.annotationProcessorFor;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.NotPublic;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

class CompositeEquivalence<T> implements Equivalence<T> {

    CompositeEquivalence(
            final PropertyFinder propertyFinder,
            final EquivalenceSpecificationValidator specificationValidator,
            final Class<T> matchedClass,
            final Class<?> matcherSpecification ) {
        this.propertyFinder = propertyFinder;
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

        addPropertyEquivalences( );

    }

    private void addPropertyEquivalences( ) {
        for ( final Method method : getSortedSpecificationMethods( ) )
            addPropertyEquivalence( method );
    }

    private Iterable<Method> getSortedSpecificationMethods( ) {

        final List<Method> methods = newArrayList( matcherSpecification.getMethods( ) );

        Collections.sort( methods, Ordering.<String>natural( ).onResultOf( new Function<Method, String>( ) {
            public String apply( final Method method ) {
                return method.getName( );
            }
        } ) );

        return methods;
    }

    private void addPropertyEquivalence( final Method specificationMethod ) {

        final Method property = findMatchingProperty( specificationMethod );

        final EquivalenceAnnotationProcessor<T> processor =
                annotationProcessorFor( matchedClass, specificationMethod, property );

        final Equivalence<T> propertyEquivalence =
                processor.processEquivalenceSpecification( );

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

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final List<Equivalence<T>> propertyEquivalences = newArrayList( );

}
