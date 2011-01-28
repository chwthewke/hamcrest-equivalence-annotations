package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link BySpecification} annotation specifies equivalence as defined by a specification interface appropriate to the annotated
 * property's type.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface BySpecification {
    /**
     * The specification interface used to specify equivalence on values of the annotated property.
     */
    Class<?> value( );
}
