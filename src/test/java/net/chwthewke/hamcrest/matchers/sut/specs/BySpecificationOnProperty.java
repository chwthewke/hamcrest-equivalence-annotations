package net.chwthewke.hamcrest.matchers.sut.specs;

import net.chwthewke.hamcrest.annotations.BySpecification;
import net.chwthewke.hamcrest.annotations.EquivalenceSpecificationOn;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPropertyWithDefinedEquivalence;
import net.chwthewke.hamcrest.matchers.sut.classes.WithPublicProperty;

@EquivalenceSpecificationOn( WithPropertyWithDefinedEquivalence.class )
public interface BySpecificationOnProperty {
    @BySpecification( EqualityOnString.class )
    WithPublicProperty getValue( );
}
