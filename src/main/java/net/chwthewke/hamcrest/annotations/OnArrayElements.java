package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link OnArrayElements} annotation modifies an accompanying equivalence annotation to specify
 * an equivalence on arrays such that two arrays are equivalent when their elements can be paired
 * in a way that puts equivalent elements in each pair.
 * <p>
 * This annotation can only target a method that returns an array type.
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface OnArrayElements {
    /**
     * If true, two arrays will be equivalent only if their elements are equivalent when
     * paired in the same order for both arrays.
     */
    boolean inOrder( ) default true;
}
