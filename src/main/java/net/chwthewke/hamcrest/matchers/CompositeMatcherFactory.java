package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.NotPublic;
import net.chwthewke.hamcrest.equivalence.Equivalence;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

class CompositeMatcherFactory<T> implements Equivalence<T> {

    // extract static methods to caller, restore dependency sanity
    static <T> Equivalence<T> asSpecifiedBy(
            final Class<?> matcherSpecification,
            final Class<T> matchedClass ) {

        return new CompositeMatcherFactory<T>(
                propertyFinderInstance,
                specificationValidatorInstance,
                equivalenceAnnotationReaderInstance,
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
            final EquivalenceAnnotationReader equivalenceAnnotationReader,
            final Class<T> matchedClass,
            final Class<?> matcherSpecification ) {
        this.propertyFinder = propertyFinder;
        this.equivalenceAnnotationReader = equivalenceAnnotationReader;
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

        addPropertyEquivalences();

        sortPropertyEquivalences();
    }

    private void sortPropertyEquivalences( ) {
        final Comparator<PropertyEquivalence<T, ?>> comparator =
                Ordering.<String>natural( ).onResultOf(
                    new Function<PropertyEquivalence<T, ?>, String>( ) {
                        public String apply( final PropertyEquivalence<T, ?> expectedPropertyTemplate ) {
                            return expectedPropertyTemplate.getPropertyName( );
                        }
                    } );

        Collections.sort( propertyEquivalences, comparator );
    }

    private void addPropertyEquivalences( ) {
        for ( final Method method : matcherSpecification.getMethods( ) )
            addPropertyEquivalence( method );
    }

    private void addPropertyEquivalence( final Method specificationMethod ) {

        final Method property = findMatchingProperty( specificationMethod );

        final PropertyEquivalence<T, ?> propertyEquivalence =
                equivalenceAnnotationReader.createPropertyEquivalence( specificationMethod, property );

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
    private final EquivalenceAnnotationReader equivalenceAnnotationReader;

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final List<PropertyEquivalence<T, ?>> propertyEquivalences = newArrayList( );

    private static final PropertyFinder propertyFinderInstance = new PropertyFinder( );
    private static final EquivalenceSpecificationValidator specificationValidatorInstance = new EquivalenceSpecificationValidator( );
    private static final EquivalenceAnnotationReader equivalenceAnnotationReaderInstance =
            new EquivalenceAnnotationReader( new EquivalenceAnnotationInterpreters( ) );

}
