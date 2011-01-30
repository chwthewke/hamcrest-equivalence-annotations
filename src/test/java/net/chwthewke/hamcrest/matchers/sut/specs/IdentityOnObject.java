package net.chwthewke.hamcrest.matchers.sut.specs;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.matchers.sut.classes.WithObjectProperty;

@EquivalenceSpecificationOn( WithObjectProperty.class )
public interface IdentityOnObject {
    @Identity
    Object getValue( );
}
