package net.chwthewke.hamcrest.sut.specs;

import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.sut.classes.WithPropertyWithDefinedEquivalence;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPropertyWithDefinedEquivalence.class )
public interface BySpecificationOnProperty {
    @BySpecification( EqualityOnString.class )
    WithPublicProperty getValue( );
}
