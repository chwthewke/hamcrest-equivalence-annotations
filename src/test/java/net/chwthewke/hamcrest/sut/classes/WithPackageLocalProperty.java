package net.chwthewke.hamcrest.sut.classes;

public class WithPackageLocalProperty {

    public WithPackageLocalProperty( final String value ) {
        super( );
        this.value = value;
    }

    String getValue( ) {
        return value;
    }

    int getIntValue( ) {
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
