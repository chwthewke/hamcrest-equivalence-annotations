package net.chwthewke.hamcrest.matchers.finder;

public class WithPublicProperty {

    public WithPublicProperty( final String value ) {
        super( );
        this.value = value;
    }

    public String getValue( ) {
        return value;
    }

    private final String value;

}
