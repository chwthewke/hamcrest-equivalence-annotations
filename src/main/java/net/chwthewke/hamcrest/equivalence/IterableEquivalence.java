package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public final class IterableEquivalence<T> implements Equivalence<Iterable<? extends T>> {

    public static <T> Equivalence<Iterable<? extends T>> equivalentElements( final Equivalence<T> equivalence ) {
        return new IterableEquivalence<T>( equivalence, true );
    }

    public static <T> Equivalence<Iterable<? extends T>> equivalentElementsInAnyOrder( final Equivalence<T> equivalence ) {
        return new IterableEquivalence<T>( equivalence, true );
    }

    private IterableEquivalence( final Equivalence<T> equivalence, final boolean inOrder ) {
        this.equivalence = equivalence;
        this.inOrder = inOrder;
    }

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

    private final boolean inOrder;
    private final Equivalence<T> equivalence;

}
