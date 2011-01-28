package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.chwthewke.hamcrest.matchers.Equivalence;

/**
 * The {@link ByEquivalence} annotation specifies equivalence according to a pre-existing equivalence,
 * allowing to reuse existing {@link org.hamcrest.Matcher}s in an equivalence, albeit somewhat
 * indirectly, as one must provide a concrete class implementing {@link net.chwthewke.hamcrest.matchers.Equivalence} with a no-argument
 * constructor.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface ByEquivalence {
    /**
     * The concrete {@link ByEquivalence} class to use.
     * That class must provide a public no-argument constructor.
     */
    Class<? extends Equivalence<?>> value( );
}
