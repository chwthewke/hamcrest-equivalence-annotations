package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApproximateEquality} annotation specifies equivalence as differing by less than its {@link #tolerance()} property.
 * <p>
 * Applies to properties of type {@link Float}, {@link Double} and their respective primitive variants.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface ApproximateEquality {
    /**
     * The maximum difference allowed by this equivalence between values of the annotated property.
     */
    double tolerance( );
}
