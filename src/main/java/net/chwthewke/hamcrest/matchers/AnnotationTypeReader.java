package net.chwthewke.hamcrest.matchers;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.Text;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

class AnnotationTypeReader {

    @Deprecated
    public Class<? extends Annotation> getEquivalenceAnnotationType( final Method specificationMethod ) {

        return getEquivalenceAnnotation( specificationMethod ).annotationType( );
    }

    public Annotation getEquivalenceAnnotation( final Method specificationMethod ) {
        final Predicate<Class<? extends Annotation>> isOnSpecification =
                new Predicate<Class<? extends Annotation>>( ) {
                    public boolean apply( final Class<? extends Annotation> input ) {
                        return specificationMethod.isAnnotationPresent( input );
                    }
                };

        final Collection<Class<? extends Annotation>> annotationsOnSpecification =
                Collections2.filter( exclusiveEquivalenceAnnotations, isOnSpecification );

        final int annotationCount = annotationsOnSpecification.size( );

        if ( annotationCount == 0 )
            return DEFAULT_EQUALITY;

        if ( annotationCount == 1 )
            return specificationMethod.getAnnotation( Iterables.get( annotationsOnSpecification, 0 ) );

        throw new IllegalArgumentException(
            String.format(
                "The equivalence specification property %s has these mutually exclusive annotations: %s.",
                specificationMethod, newArrayList( annotationsOnSpecification ) ) );
    }

    @SuppressWarnings( "unchecked" )
    private static final Collection<Class<? extends Annotation>> exclusiveEquivalenceAnnotations =
            Lists.<Class<? extends Annotation>>newArrayList(
                Equality.class,
                Identity.class,
                ApproximateEquality.class,
                BySpecification.class,
                ByEquivalence.class,
                Text.class );

    private static final Equality DEFAULT_EQUALITY = new Equality( ) {
        public Class<? extends Annotation> annotationType( ) {
            return Equality.class;
        }
    };
}
