package net.chwthewke.hamcrest.matchers;

import net.chwthewke.hamcrest.equivalence.ArrayEquivalences;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.IterableEquivalence;

class ContainerEquivalenceFactory {
    public <T> Equivalence<Iterable<? extends T>> iterableEquivalence(
            final Equivalence<? super T> equivalenceOnElementType, final boolean enforceOrder ) {
        return new IterableEquivalence<T>( equivalenceOnElementType, enforceOrder );
    }

    public <T> Equivalence<T[ ]> arrayEquivalence( final Equivalence<? super T> equivalence, final boolean inOrder ) {
        return ArrayEquivalences.arrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<boolean[ ]> booleanArrayEquivalence( final Equivalence<? super Boolean> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.booleanArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<byte[ ]> byteArrayEquivalence( final Equivalence<? super Byte> equivalence, final boolean inOrder ) {
        return ArrayEquivalences.byteArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<char[ ]> charArrayEquivalence( final Equivalence<? super Character> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.charArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<double[ ]> doubleArrayEquivalence( final Equivalence<? super Double> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.doubleArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<float[ ]> floatArrayEquivalence( final Equivalence<? super Float> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.floatArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<int[ ]> intArrayEquivalence( final Equivalence<? super Integer> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.intArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<long[ ]> longArrayEquivalence( final Equivalence<? super Long> equivalence, final boolean inOrder ) {
        return ArrayEquivalences.longArrayEquivalence( equivalence, inOrder );
    }

    public Equivalence<short[ ]> shortArrayEquivalence( final Equivalence<? super Short> equivalence,
            final boolean inOrder ) {
        return ArrayEquivalences.shortArrayEquivalence( equivalence, inOrder );
    }
}
