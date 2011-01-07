package net.chwthewke.hamcrest.matchers.use_case_classes;

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
