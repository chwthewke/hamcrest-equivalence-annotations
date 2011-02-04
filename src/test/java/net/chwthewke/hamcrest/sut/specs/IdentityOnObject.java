package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.sut.classes.WithObjectProperty;

@EquivalenceSpecificationOn( WithObjectProperty.class )
public interface IdentityOnObject {
    @Identity
    Object getValue( );
}
