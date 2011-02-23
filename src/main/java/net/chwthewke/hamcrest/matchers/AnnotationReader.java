package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Iterables.transform;
import static net.chwthewke.hamcrest.matchers.TypeEquivalenceSpecification.createTypeEquivalenceSpecification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.OnArrayElements;
import net.chwthewke.hamcrest.annotations.OnIterableElements;
import net.chwthewke.hamcrest.annotations.Text;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

class AnnotationReader {

    public TypeEquivalenceSpecification<?> getTypeEquivalenceSpecification( final Method specificationMethod ) {
        final Class<?> targetType = specificationMethod.getReturnType( );
        final Annotation equivalenceAnnotation =
                getOnlyAnnotationAmongTypes( equivalenceAnnotations, DEFAULT_EQUALITY, specificationMethod );
        final Annotation containerAnnotation =
                getOnlyAnnotationAmongTypes( containerAnnotations, null, specificationMethod );
        return createTypeEquivalenceSpecification( targetType, equivalenceAnnotation, containerAnnotation );
    }

    @Deprecated
    public Annotation getEquivalenceAnnotation( final Method specificationMethod ) {
        return getOnlyAnnotationAmongTypes( equivalenceAnnotations, DEFAULT_EQUALITY, specificationMethod );
    }

    private Annotation getOnlyAnnotationAmongTypes( final Iterable<Class<? extends Annotation>> annotationTypes,
            final Annotation defaultValue, final Method specificationMethod ) {

        final Function<Class<? extends Annotation>, Annotation> extractFunction =
                new Function<Class<? extends Annotation>, Annotation>( ) {
                    public Annotation apply( final Class<? extends Annotation> annotationType ) {
                        return specificationMethod.getAnnotation( annotationType );
                    }
                };

        final Iterable<Annotation> eligibleAnnotations =
                filter( transform( annotationTypes, extractFunction ), notNull( ) );

        try
        {
            return getOnlyElement( eligibleAnnotations, defaultValue );
        }
        catch ( final IllegalArgumentException e )
        {
            throw new IllegalArgumentException( String.format(
                "The equivalence specification property %s has these mutually exclusive annotations: %s.",
                specificationMethod.getName( ),
                getAnnotationNames( eligibleAnnotations ) ) );
        }
    }

    private Iterable<String> getAnnotationNames( final Iterable<Annotation> eligibleAnnotations ) {
        return transform( eligibleAnnotations, new Function<Annotation, String>( ) {
            public String apply( final Annotation annotation ) {
                return annotation.annotationType( ).getSimpleName( );
            }
        } );
    }

    @SuppressWarnings( "unchecked" )
    private static final Collection<Class<? extends Annotation>> equivalenceAnnotations =
            Lists.<Class<? extends Annotation>>newArrayList(
                Equality.class,
                Identity.class,
                ApproximateEquality.class,
                BySpecification.class,
                ByEquivalence.class,
                Text.class );

    @SuppressWarnings( "unchecked" )
    private static final Collection<Class<? extends Annotation>> containerAnnotations =
            Lists.<Class<? extends Annotation>>newArrayList(
                OnArrayElements.class,
                OnIterableElements.class );

    private static final Equality DEFAULT_EQUALITY = new Equality( ) {
        public Class<? extends Annotation> annotationType( ) {
            return Equality.class;
        }
    };
}
