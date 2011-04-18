package net.chwthewke.hamcrest.sut.classes;

public class WithDoubleProperty {

    public WithDoubleProperty( final double value ) {
        this.value = value;
    }

    public double getValue( ) {
        return value;
    }

    private final double value;
}
