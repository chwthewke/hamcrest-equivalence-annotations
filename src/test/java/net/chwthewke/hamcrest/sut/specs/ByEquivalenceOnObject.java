package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ByEquivalence;

public interface ByEquivalenceOnObject {
    @ByEquivalence( ObjectEqualityEquivalence.class )
    Object getValue( );
}
