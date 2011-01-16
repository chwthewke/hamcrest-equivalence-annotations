package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used with an equivalence annotation, {@link NotPublic} declares that the annotated method should be used for equivalence
 * even though it is not public, but merely visible, on the matched type.
 * 
 * @see MatcherOf
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface NotPublic {
}
