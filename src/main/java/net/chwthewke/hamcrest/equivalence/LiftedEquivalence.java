package net.chwthewke.hamcrest.equivalence;

import static com.google.common.base.Preconditions.checkNotNull;

import org.hamcrest.Matcher;

import com.google.common.base.Function;

public final class LiftedEquivalence<T, U> implements Equivalence<T> {

    private LiftedEquivalence( final String propertyName,
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

    public static <T, U> LiftedEquivalence<T, U> create(
            final String propertyName,
            final Function<T, U> propertyMethod,
            final Equivalence<? super U> equivalence ) {
        return new LiftedEquivalence<T, U>( propertyName, propertyMethod, equivalence );
    }

    private final String propertyName;
    private final Function<T, U> propertyMethod;
    private final Equivalence<? super U> equivalence;

}
