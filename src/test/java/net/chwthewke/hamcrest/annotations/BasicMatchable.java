package net.chwthewke.hamcrest.annotations;

public class BasicMatchable {
    public BasicMatchable( final Object value ) {
        super( );
        this.value = value;
    }

    public Object getValue( ) {
        return value;
    }

    private final Object value;
}
