package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.chwthewke.hamcrest.MatcherFactory;
import net.chwthewke.hamcrest.MatcherSpecification;
import net.chwthewke.hamcrest.annotations.MatcherOf;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

class CompositeMatcherFactory<T> implements MatcherFactory<T> {

    static <T> MatcherFactory<T> asSpecifiedBy( final Class<?> matcherSpecification,
            final Class<T> matchedClass ) {
        return new CompositeMatcherFactory<T>( matchedClass, matcherSpecification );
    }

    @SuppressWarnings( "unchecked" )
    static <T> MatcherFactory<T>
            asSpecifiedBy( final Class<? extends MatcherSpecification<T>> matcherSpecification ) {
        return asSpecifiedBy( matcherSpecification,
            (Class<T>) checkAndGetMatchedClassInAnnotation( matcherSpecification ) );
    }

    CompositeMatcherFactory( final Class<T> matchedClass, final Class<?> matcherSpecification ) {
        this.matchedClass = checkNotNull( matchedClass );
        this.matcherSpecification = checkNotNull( matcherSpecification );

        initialize( );
    }

    public Matcher<T> equivalentTo( final T expected ) {
        return new CompositeMatcher<T>( matchedClass, expectedPropertyTemplates, expected );
    }

    private void initialize( ) {
        checkSpecificationIsAnInterface( );

        checkSpecificationForAnnotation( );

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
        final Method[ ] specificationMethods = matcherSpecification.getMethods( );
        for ( final Method method : specificationMethods )
            addExpectedPropertyTemplate( method );
    }

    private void addExpectedPropertyTemplate( final Method specificationMethod ) {
        checkValidProperty( specificationMethod );

        final Class<?> propertyType = specificationMethod.getReturnType( );

        final String propertyName = specificationMethod.getName( );

        final Method property = getAndCheckProperty( propertyName, propertyType );

        final ExpectedPropertyTemplate<T, ?> expectedPropertyTemplate =
                new ExpectedPropertyTemplateFactory<T>( property, specificationMethod )
                    .getExpectedPropertyTemplate( );
        expectedPropertyTemplates.add( expectedPropertyTemplate );
    }

    private Method getAndCheckProperty( final String propertyName, final Class<?> propertyType ) {
        final Method property;
        try
        {
            property = matchedClass.getMethod( propertyName );
        }
        catch ( final NoSuchMethodException e )
        {
            throw new IllegalArgumentException(
                String.format( "The matched class %s lacks the property method '%s()' present on %s.",
                    matchedClass.getName( ),
                    propertyName,
                    matcherSpecification.getName( ) ), e );
        }

        if ( !propertyType.isAssignableFrom( property.getReturnType( ) ) )
            throw new IllegalArgumentException(
                String.format(
                    "The property '%s()' on %s has return type %s which is not assignable to %s as specified on %s.",
                    propertyName, matchedClass.getName( ),
                    property.getReturnType( ).getName( ), propertyType.getName( ),
                    matcherSpecification.getName( ) ) );

        return property;
    }

    private void checkValidProperty( final Method method ) {
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

    private void checkSpecificationIsAnInterface( ) {
        if ( !matcherSpecification.isInterface( ) )
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must be an interface.",
                    matcherSpecification.getName( ) ) );
    }

    private void checkSpecificationForAnnotation( ) {
        final Class<?> value = checkAndGetMatchedClassInAnnotation( matcherSpecification );
        if ( value != matchedClass )
            throw new IllegalArgumentException(
                String.format( "The %s annotation on %s must have a value of %s.",
                    MatcherOf.class.getSimpleName( ),
                    matcherSpecification.getName( ),
                    matchedClass.getName( ) ) );
    }

    private static Class<?> checkAndGetMatchedClassInAnnotation( final Class<?> specification ) {
        final MatcherOf annotation = specification.getAnnotation( MatcherOf.class );
        if ( annotation == null )
        {
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must be annotated with %s.",
                    specification.getName( ),
                    MatcherOf.class.getSimpleName( ) ) );
        }
        return annotation.value( );
    }

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final List<ExpectedPropertyTemplate<T, ?>> expectedPropertyTemplates = newArrayList( );

}
