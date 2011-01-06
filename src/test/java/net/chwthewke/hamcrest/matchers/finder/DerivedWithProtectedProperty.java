package net.chwthewke.hamcrest.matchers.finder;

public class DerivedWithProtectedProperty extends BaseWithProtectedProperty {

    public DerivedWithProtectedProperty( final String value ) {
        super( value );
    }

    @Override
    protected String getValue( ) {
        return super.getValue( ) + "0";
    }

}
