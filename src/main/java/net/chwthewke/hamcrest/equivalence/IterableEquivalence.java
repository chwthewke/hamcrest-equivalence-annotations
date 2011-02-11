package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

final class IterableEquivalence<T> implements Equivalence<Iterable<? extends T>> {

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

    IterableEquivalence( final Equivalence<T> equivalence, final boolean inOrder ) {
        this.equivalence = equivalence;
        this.inOrder = inOrder;
    }

    private final boolean inOrder;
    private final Equivalence<T> equivalence;

}
