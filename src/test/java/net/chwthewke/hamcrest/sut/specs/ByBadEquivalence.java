package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.equivalences.BadEquivalence;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface ByBadEquivalence {

    @ByEquivalence( BadEquivalence.class )
    int getIntValue( );

}
