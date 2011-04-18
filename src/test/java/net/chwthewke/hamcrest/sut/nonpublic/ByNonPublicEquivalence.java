package net.chwthewke.hamcrest.sut.nonpublic;

import net.chwthewke.hamcrest.annotations.ByEquivalence;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface ByNonPublicEquivalence {

    @ByEquivalence( NonPublicEquivalence.class )
    int getIntValue( );

}
