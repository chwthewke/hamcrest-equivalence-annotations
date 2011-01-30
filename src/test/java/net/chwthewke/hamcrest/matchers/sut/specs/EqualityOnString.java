package net.chwthewke.hamcrest.matchers.sut.specs;

import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface EqualityOnString {
    @Equality
    String getValue( );
}
