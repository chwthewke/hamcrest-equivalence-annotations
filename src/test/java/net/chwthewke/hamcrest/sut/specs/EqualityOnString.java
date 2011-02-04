package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface EqualityOnString {
    @Equality
    String getValue( );
}
