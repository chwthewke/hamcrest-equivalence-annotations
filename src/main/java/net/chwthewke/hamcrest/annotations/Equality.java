package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Equality} annotation specifies equivalence as the result of calling {@link Object#equals(Object)}.
 * <p>
 * It is interpreted into an instance of {@link net.chwthewke.hamcrest.equivalence.EqualityEquivalence}.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Equality {
}
