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
        return new CompositeMatcher<T>( matchedClass, expectedPropertyTemplates, expected );
    }

    private void initialize( ) {
        checkSpecificationTargetClass( );

        addExpectedPropertyTemplates( );

        sortExpectedPropertyTemplates( );
    }

    private void sortExpectedPropertyTemplates( ) {
        final Comparator<ExpectedPropertyTemplate<T, ?>> comparator =
                Ordering.<String>natural( ).onResultOf(
                    new Function<ExpectedPropertyTemplate<T, ?>, String>( ) {
                        public String apply( final ExpectedPropertyTemplate<T, ?> expectedPropertyTemplate ) {
                            return expectedPropertyTemplate.getPropertyName( );
                        }
                    } );

        Collections.sort( expectedPropertyTemplates, comparator );
    }

    private void addExpectedPropertyTemplates( ) {
        for ( final Method method : matcherSpecification.getMethods( ) )
            addExpectedPropertyTemplate( method );
    }

    private void addExpectedPropertyTemplate( final Method specificationMethod ) {

        final Method property = findMatchingProperty( specificationMethod );

        final ExpectedPropertyTemplate<T, ?> expectedPropertyTemplate =
                new ExpectedPropertyTemplateFactory<T>( propertyFinder,
                        specificationValidator,
                        property,
                        specificationMethod )
                    .getExpectedPropertyTemplate( );
        expectedPropertyTemplates.add( expectedPropertyTemplate );
    }

    private Method findMatchingProperty( final Method specificationMethod ) {
        // TODO wrap errors with specification class/method info.

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
    private final List<ExpectedPropertyTemplate<T, ?>> expectedPropertyTemplates = newArrayList( );

    private static final PropertyFinder propertyFinderInstance = new PropertyFinder( );
    private static final EquivalenceSpecificationValidator specificationValidatorInstance = new EquivalenceSpecificationValidator( );
}
