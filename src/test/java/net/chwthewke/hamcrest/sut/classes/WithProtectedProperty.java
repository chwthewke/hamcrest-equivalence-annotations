package net.chwthewke.hamcrest.sut.classes;

public class WithProtectedProperty {

    public WithProtectedProperty( final String value ) {
        super( );
        this.value = value;
    }

    protected String getValue( ) {
        return value;
    }

    protected int getIntValue( ) {
        try
        {
            return Integer.parseInt( value );
        }
        catch ( final NumberFormatException e )
        {
            return Integer.MIN_VALUE;
        }
    }

    private final String value;

}
