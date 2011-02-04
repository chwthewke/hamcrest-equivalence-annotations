package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.equivalences.AbstractSimpleEquivalence;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface ByAbstractSimpleEquivalence {

    @ByEquivalence( AbstractSimpleEquivalence.class )
    int getIntValue( );

}
