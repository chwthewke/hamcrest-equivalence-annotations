package net.chwthewke.hamcrest.annotations.att2;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import net.chwthewke.hamcrest.annotations.Approximate;
import net.chwthewke.hamcrest.annotations.Identical;
import net.chwthewke.hamcrest.annotations.MatcherOf;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

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
        return new CompositeMatcher<T>( matchedClass, subMatcherProviders, expected );
    }

    private void initializeSubMatcherProviders( ) {
        checkSpecificationIsAnInterface( );

        checkSpecificationForAnnotation( );

        final Method[ ] specificationMethods = matcherSpecification.getMethods( );
        for ( final Method method : specificationMethods )
            addSubMatcherProvider( method );
    }

    private void addSubMatcherProvider( final Method specificationMethod ) {
        checkValidProperty( specificationMethod );

        final Class<?> propertyType = specificationMethod.getReturnType( );

        final String propertyName = specificationMethod.getName( );

        final Method property = getAndCheckProperty( propertyName, propertyType );

        // tmp conditional dispatch of matcher selection
        SubMatcherProvider<T, ?> subMatcherProvider;
        if ( specificationMethod.isAnnotationPresent( Approximate.class ) )
        {
            subMatcherProvider = createApproximateSubMatcher( property, propertyName,
                specificationMethod.getAnnotation( Approximate.class ).value( ) );
        }
        else if ( specificationMethod.isAnnotationPresent( Identical.class ) )
        {
            subMatcherProvider = createIdentitySubMatcher( property, propertyName, propertyType );
        }
        else
        {
            subMatcherProvider = createEqualitySubMatcher( property, propertyName, propertyType );
        }
        subMatcherProviders.add( subMatcherProvider );
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
                    property.getReturnType( ), propertyType,
                    matcherSpecification.getName( ) ) );

        return property;
    }

    private SubMatcherProvider<T, Double> createApproximateSubMatcher( final Method property,
            final String propertyName,
            final double tolerance ) {
        return SubMatcherFactory.closeTo( propertyName,
            propertyFunction( property, Double.class ), tolerance );
    }

    private <U> SubMatcherProvider<T, U> createIdentitySubMatcher( final Method property,
            final String propertyName,
            final Class<U> propertyType ) {
        return SubMatcherFactory.<T, U>sameInstance( propertyName,
            propertyFunction( property, propertyType ) );
    }

    private <U> SubMatcherProvider<T, U> createEqualitySubMatcher( final Method property,
            final String propertyName,
            final Class<U> propertyType ) {

        return SubMatcherFactory.<T, U>equalTo( propertyName,
            propertyFunction( property, propertyType ) );
    }

    // TODO something can, and will go wrong if propertyType is property.getReturnType() and primitive.
    private <U> Function<T, U> propertyFunction( final Method property, final Class<U> propertyType ) {
        return new Function<T, U>( ) {
            public U apply( final T item ) {
                return extractProperty( propertyType, property, item );
            }
        };
    }

    private <U> U extractProperty( final Class<U> propertyType,
            final Method property, final T item ) {

        try
        {
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
    private final Collection<SubMatcherProvider<T, ?>> subMatcherProviders = newArrayList( );
}
