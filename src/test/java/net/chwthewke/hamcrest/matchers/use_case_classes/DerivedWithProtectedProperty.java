package net.chwthewke.hamcrest.matchers.use_case_classes;

public class DerivedWithProtectedProperty extends WithProtectedProperty {

    public DerivedWithProtectedProperty( final String value ) {
        super( value );
    }

    @Override
    protected String getValue( ) {
        return super.getValue( ) + "0";
    }

}
