package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

/**
 * {@link IterableEquivalence} is an equivalence that acts upon Iterables of a type given an equivalence on
 * the component type. The resulting equivalence may optionally ignore the order of elements in the sequences it
 * receives.
 * 
 * @param <T>
 *            The component type.
 */
public final class IterableEquivalence<T> implements Equivalence<Iterable<? extends T>> {

    /**
     * Partial evaluation.
     * 
     * @param expected
     *            An iterable of the component type.
     * @return A {@link Matcher} that matches {@link Iterable}s equivalent to <code>expected</code>.
     */
    public Matcher<Iterable<? extends T>> equivalentTo( final Iterable<? extends T> expected ) {
        final List<Matcher<? super T>> expectedItems =
                transform( newArrayList( expected ),
                    new Function<T, Matcher<? super T>>( ) {
                        public Matcher<? super T> apply( final T input ) {
                            return equivalence.equivalentTo( input );
                        }
                    } );
        return inOrder ? contains( expectedItems ) : containsInAnyOrder( expectedItems );
    }

    /**
     * Creates an equivalence such that two {@link Iterable}s are equivalent iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent.
     * 
     * @param equivalence
     *            The equivalence on the component type.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must respect iteration order.
     */
    public IterableEquivalence( final Equivalence<? super T> equivalence, final boolean inOrder ) {
        this.equivalence = equivalence;
        this.inOrder = inOrder;
    }

    private final boolean inOrder;
    private final Equivalence<? super T> equivalence;

}
