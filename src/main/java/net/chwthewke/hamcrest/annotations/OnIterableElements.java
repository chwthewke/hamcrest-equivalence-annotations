package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link OnIterableElements} annotation modifies an accompanying equivalence annotation to specify
 * an equivalence on {@link Iterable}s such that two {@link Iterable}s are equivalent when their elements can be paired
 * in a way that puts equivalent elements in each pair.
 * <p>
 * This annotation can only target a method that returns a type that implements {@link Iterable}.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface OnIterableElements {
    /**
     * Optionally specifies the type of elements in the {@link Iterable}, or a supertype thereof.
     */
    Class<?> elementType( ) default Object.class;

    /**
     * If true, two {@link Iterable}s will be equivalent only if their elements are equivalent when
     * paired in the same order for both {@link Iterable}s.
     */
    boolean inOrder( ) default true;
}
