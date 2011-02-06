package net.chwthewke.hamcrest.equivalence;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public class LiftedEquivalence<T, U> implements Equivalence<T> {

    public LiftedEquivalence( final String propertyName,
                                 final Function<T, U> propertyMethod,
                                 final Equivalence<? super U> equivalence ) {
        this.propertyName = checkNotNull( propertyName );
        this.propertyMethod = checkNotNull( propertyMethod );
        this.equivalence = equivalence;
    }

    public Matcher<T> equivalentTo( final T expected ) {
        return new LiftedMatcher<T, U>( propertyName, propertyMethod,
                equivalence.equivalentTo( propertyMethod.apply( expected ) ) );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Equivalence<? super U> equivalence;

}
