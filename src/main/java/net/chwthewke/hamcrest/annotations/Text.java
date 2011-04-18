package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.chwthewke.hamcrest.equivalence.TextEquivalenceOption;

/**
 * The {@link Text} annotation specifies equivalence as equality on {@link String}s up to a set of options.
 * <p>
 * It is interpreted with {@link net.chwthewke.hamcrest.equivalence.TextEquivalence#textEquivalenceWith(TextEquivalenceOption...)}.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Text {
    /**
     * The options used to relax the equivalence, starting from equality.
     */
    TextEquivalenceOption[ ] options( ) default { };
}
