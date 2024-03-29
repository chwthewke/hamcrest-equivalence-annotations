package net.chwthewke.hamcrest.sut.classes;

public class WithPropertyWithDefinedEquivalence {

    public WithPropertyWithDefinedEquivalence( final WithPublicProperty value ) {
        this.value = value;
    }

    public WithPublicProperty getValue( ) {
        return value;
    }

    private final WithPublicProperty value;
}
