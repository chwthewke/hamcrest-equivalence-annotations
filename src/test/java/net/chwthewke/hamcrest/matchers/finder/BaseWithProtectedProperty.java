package net.chwthewke.hamcrest.matchers.finder;

public class BaseWithProtectedProperty {

    public BaseWithProtectedProperty( final String value ) {
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
