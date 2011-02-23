package net.chwthewke.hamcrest.matchers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;

final class TypeEquivalenceSpecification<T> {

    public static <T> TypeEquivalenceSpecification<T> createTypeEquivalenceSpecification( final Class<T> targetType,
            final Annotation equivalenceAnnotation, final Annotation containerAnnotation ) {
        return new TypeEquivalenceSpecification<T>( targetType, equivalenceAnnotation, containerAnnotation );
    }

    public Annotation getEquivalenceAnnotation( ) {
        return equivalenceAnnotation;
    }

    public boolean hasContainerAnnotation( ) {
        return containerAnnotation != null;
    }

    public Annotation getContainerAnnotation( ) {
        return checkNotNull( containerAnnotation );
    }

    public Class<T> getTargetType( ) {
        return targetType;
    }

    private TypeEquivalenceSpecification( final Class<T> targetType,
            final Annotation equivalenceAnnotation,
            final Annotation containerAnnotation ) {
        this.targetType = targetType;
        this.equivalenceAnnotation = equivalenceAnnotation;
        this.containerAnnotation = containerAnnotation;
    }

    private final Annotation equivalenceAnnotation;
    private final Annotation containerAnnotation;

    private final Class<T> targetType;

}
