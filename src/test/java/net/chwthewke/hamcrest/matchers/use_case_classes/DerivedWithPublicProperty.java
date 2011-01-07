package net.chwthewke.hamcrest.matchers.use_case_classes;

public class DerivedWithPublicProperty extends BaseWithPublicProperty {

    public DerivedWithPublicProperty( final String value ) {
        super( value );
    }

    @Override
    public String getValue( ) {
        return super.getValue( ) + "0";
    }

}
