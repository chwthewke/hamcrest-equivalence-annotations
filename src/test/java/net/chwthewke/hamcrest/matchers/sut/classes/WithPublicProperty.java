package net.chwthewke.hamcrest.matchers.sut.classes;

public class WithPublicProperty {

    public WithPublicProperty( final String value ) {
        super( );
        this.value = value;
    }

    public String getValue( ) {
        return value;
    }

    public int getIntValue( ) {
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
