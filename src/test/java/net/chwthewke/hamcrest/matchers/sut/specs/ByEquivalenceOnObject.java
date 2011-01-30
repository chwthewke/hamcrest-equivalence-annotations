package net.chwthewke.hamcrest.matchers.sut.specs;

import net.chwthewke.hamcrest.annotations.ByEquivalence;

public interface ByEquivalenceOnObject {
    @ByEquivalence( ObjectEqualityEquivalence.class )
    Object getValue( );
}
