package net.chwthewke.hamcrest.matchers.finder;

public class DerivedWithPublicProperty extends BaseWithPublicProperty {

    public DerivedWithPublicProperty( final String value ) {
        super( value );
    }

    @Override
    public String getValue( ) {
        return super.getValue( ) + "0";
    }

}
