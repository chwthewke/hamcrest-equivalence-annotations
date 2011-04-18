package net.chwthewke.hamcrest.sut.classes;

public class WithBadProperty {

    public WithBadProperty( final String value ) {
        this.value = value;
    }

    public String getValue( ) {
        throw new RuntimeException( value );
    }

    private final String value;

}
