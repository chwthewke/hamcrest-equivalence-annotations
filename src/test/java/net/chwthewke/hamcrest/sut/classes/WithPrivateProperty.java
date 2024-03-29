package net.chwthewke.hamcrest.sut.classes;

public class WithPrivateProperty {

    public WithPrivateProperty( final long value ) {
        super( );
        this.value = value;
    }

    @SuppressWarnings( "unused" )
    private long getValue( ) {
        return value;
    }

    private final long value;
}
