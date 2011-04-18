package net.chwthewke.hamcrest.sut.classes;

public class DerivedWithPublicProperty extends WithPublicProperty {

    public DerivedWithPublicProperty( final String value ) {
        super( value );
    }

    @Override
    public String getValue( ) {
        return super.getValue( ) + "0";
    }

}
