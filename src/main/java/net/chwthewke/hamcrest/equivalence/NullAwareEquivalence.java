package net.chwthewke.hamcrest.equivalence;

import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.Matcher;

public final class NullAwareEquivalence<T> implements Equivalence<T> {

    public static <T> NullAwareEquivalence<T> nullAware( final Equivalence<T> delegate, final Class<T> targetType ) {
        return new NullAwareEquivalence<T>( delegate, targetType );
    }

    public Matcher<T> equivalentTo( final T expected ) {
        return expected == null ? nullValue( targetType ) : delegate.equivalentTo( expected );
    }

    private NullAwareEquivalence( final Equivalence<T> delegate, final Class<T> targetType ) {
        this.delegate = delegate;
        this.targetType = targetType;
    }

    private final Equivalence<T> delegate;
    private final Class<T> targetType;
}
