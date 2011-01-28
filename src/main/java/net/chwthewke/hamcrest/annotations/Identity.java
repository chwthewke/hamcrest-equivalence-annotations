package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Identity} annotation specifies equivalence as reference equality (<code>==</code>).
 * <p>
 * Note: values of primitive types are boxed, then compared with {@link Object#equals(Object)}.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Identity {
}
