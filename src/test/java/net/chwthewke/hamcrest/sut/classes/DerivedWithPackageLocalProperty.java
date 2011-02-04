package net.chwthewke.hamcrest.sut.classes;

public class DerivedWithPackageLocalProperty extends WithPackageLocalProperty {

    public DerivedWithPackageLocalProperty( final String value ) {
        super( value );
    }

    @Override
    String getValue( ) {
        return super.getValue( ) + "0";
    }

}
