package net.chwthewke.hamcrest.matchers.sut.specs;

import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPublicProperty.class )
public interface IdentityOnPrimitive {
    @Identity
    int getIntValue( );
}
