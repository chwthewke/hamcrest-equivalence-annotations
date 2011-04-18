package net.chwthewke.hamcrest.visibility;

class PackageLocal {

    public PackageLocal( final String value ) {
        this.value = value;
    }

    public String getValue( ) {
        return value;
    }

    private final String value;
}
