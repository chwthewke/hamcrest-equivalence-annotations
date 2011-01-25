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

package net.chwthewke.hamcrest.matchers;


/**
 * This class has some factory methods to create {@link Equivalence} instances from <em>equivalence specifications</em> (i.e. classes
 * annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn}).
 */
public final class EquivalenceMatchers {

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with
     *            <code>value()</code> equal to <code>matchedType</code>).
     * @param matchedType
     *            The matched type.
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     */
    public static <T> Equivalence<T> asSpecifiedBy( final Class<?> equivalenceSpecification,
            final Class<T> matchedType ) {
        return CompositeMatcherFactory.asSpecifiedBy( equivalenceSpecification, matchedType );
    }

    /**
     * Creates an {@link Equivalence} from the equivalence specification <code>equivalenceSpecification</code>.
     * 
     * @param <T>
     *            The matched type.
     * @param equivalenceSpecification
     *            The equivalence specification, which must be annotated with {@link net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn} (with
     *            <code>value()</code> equal to <code>matchedType</code>).
     * @return an {@link Equivalence} as specified by <code>equivalenceSpecification</code>.
     *         <p>
     *         Note: this convenience method allows to eschew the second parameter on the condition that the equivalence specification
     *         extend the marker interface {@link EquivalenceSpecification}. Moreover, this is less safe than the alternative, as the
     *         matched type cannot be checked at runtime.
     */
    public static <T> Equivalence<T>
            asSpecifiedBy( final Class<? extends EquivalenceSpecification<T>> equivalenceSpecification ) {
        return CompositeMatcherFactory.asSpecifiedBy( equivalenceSpecification );
    }

    private EquivalenceMatchers( ) {
    }
}
