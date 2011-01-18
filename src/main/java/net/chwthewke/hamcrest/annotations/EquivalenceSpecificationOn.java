package net.chwthewke.hamcrest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link EquivalenceSpecificationOn} annotation declares that the annotated type (which <strong>must</strong> be a <strong>public</strong> interface) is
 * an <em>equivalence specification</em> for the type returned by {@link #value()} (henceforth called <em>matched type</em>).
 * <p>
 * This imposes further restrictions on the interface, namely that each of its methods must:
 * <ul>
 * <li>have a non-<code>void</code> return type ({@link Void} is ok, though),</li>
 * <li>have no arguments</li>
 * <li>have the same signature as a <strong>public</strong> method of the matched type, with the following derogations:
 * <ul>
 * <li>if the interface method is annotated with {@link NotPublic}, the method must simply be visible from the matched type (i.e. it can be
 * declared on a superclass if protected, etc.)</li>
 * <li>covariant overriding is supported, i.e. the return type of the method on the interface may be a superclass of the return type of the
 * method on the matched type.
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Each of the methods of the interface may also be annotated with at most one annotation used to define equivalence for that property
 * method. Placing more than one such annotation on a method is unsupported and may result in an error in a later version. The available
 * equivalence annotations are:
 * <ul>
 * <li>{@link Equality},</li>
 * <li>{@link Identity},</li>
 * <li>{@link ApproximateEquality},</li>
 * <li>{@link BySpecification}</li>
 * </ul>
 * If no equivalence annotation is present on a method, it is treated as if it bore the {@link Equality} annotation.
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface EquivalenceSpecificationOn {
    /**
     * The matched type.
     */
    Class<?> value( );
}
