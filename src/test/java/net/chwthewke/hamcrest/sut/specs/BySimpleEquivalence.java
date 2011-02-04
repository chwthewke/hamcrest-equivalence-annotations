package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.equivalences.SimpleEquivalence;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface BySimpleEquivalence {

    @ByEquivalence( SimpleEquivalence.class )
    int getIntValue( );

}
