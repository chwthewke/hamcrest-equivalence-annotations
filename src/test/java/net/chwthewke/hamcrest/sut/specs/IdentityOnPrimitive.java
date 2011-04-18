package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface IdentityOnPrimitive {
    @Identity
    int getIntValue( );
}
