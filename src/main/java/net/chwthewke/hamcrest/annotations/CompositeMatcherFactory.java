package net.chwthewke.hamcrest.annotations;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hamcrest.Matcher;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

public class CompositeMatcherFactory<T> {

    // TODO rename/refactor to better fluency
    public static <T> CompositeMatcherFactory<T>
            matcherBySpecification( final Class<T> matchedClass, final Class<?> matcherSpecification ) {
        return new CompositeMatcherFactory<T>( matchedClass, matcherSpecification );
    }

    // TODO alternative factory method
    // public static <T> CompositeMatcherFactory<T>
    //      matcherBySpecification( Class<? extends MatcherSpecification<T>> matcherSpecification ) { ... }

    CompositeMatcherFactory( final Class<T> matchedClass, final Class<?> matcherSpecification ) {
        this.matchedClass = checkNotNull( matchedClass );
        this.matcherSpecification = checkNotNull( matcherSpecification );

        initializeSubMatcherProviders( );
    }

    public Matcher<T> of( final T expected ) {
        return new CompositeMatcher<T>( matchedClass, subMatcherTemplates, expected );
    }

    private void initializeSubMatcherProviders( ) {
        checkSpecificationIsAnInterface( );

        checkSpecificationForAnnotation( );

        final Method[ ] specificationMethods = matcherSpecification.getMethods( );
        for ( final Method method : specificationMethods )
            addSubMatcherTemplate( method );

        final Comparator<SubMatcherTemplate<T, ?>> comparator =
                Ordering.<String>natural( ).onResultOf(
                    new Function<SubMatcherTemplate<T, ?>, String>( ) {
                        public String apply( final SubMatcherTemplate<T, ?> subMatcherTemplate ) {
                            return subMatcherTemplate.getPropertyName( );
                        }
                    } );

        Collections.sort( subMatcherTemplates, comparator );
    }

    private void addSubMatcherTemplate( final Method specificationMethod ) {
        checkValidProperty( specificationMethod );

        final Class<?> propertyType = specificationMethod.getReturnType( );

        final String propertyName = specificationMethod.getName( );

        final Method property = getAndCheckProperty( propertyName, propertyType );

        final SubMatcherTemplate<T, ?> subMatcherTemplate =
                new SubMatcherTemplateFactory2<T>( property, specificationMethod )
                    .getSubMatcherTemplate( );
        subMatcherTemplates.add( subMatcherTemplate );
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
        final MatcherOf annotation =
                matcherSpecification.getAnnotation( MatcherOf.class );
        if ( annotation == null )
            throw new IllegalArgumentException(
                String.format( "The 'matcherSpecification' %s must be annotated with %s.",
                    matcherSpecification.getName( ),
                    MatcherOf.class.getSimpleName( ) ) );
        if ( annotation.value( ) != matchedClass )
            throw new IllegalArgumentException(
                String.format( "The %s annotation on %s must have a value of %s.",
                    MatcherOf.class.getSimpleName( ),
                    matcherSpecification.getName( ),
                    matchedClass.getName( ) ) );
    }

    private final Class<T> matchedClass;
    private final Class<?> matcherSpecification;
    private final List<SubMatcherTemplate<T, ?>> subMatcherTemplates = newArrayList( );

}
