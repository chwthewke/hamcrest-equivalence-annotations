/*
 * Copyright (c) 2010-2011, Thomas Dufour
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
