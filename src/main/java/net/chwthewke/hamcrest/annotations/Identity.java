package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Identity} annotation specifies equivalence as reference equality (<code>==</code>).
 * <p>
 * It is interpreted into an instance of {@link net.chwthewke.hamcrest.equivalence.IdentityEquivalence}, except when used with a primitive
 * type; then {@link net.chwthewke.hamcrest.equivalence.EqualityEquivalence} is used on boxed values.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Identity {
}
