package net.chwthewke.hamcrest.annotations;

@MatcherOf( GasTank.class )
public interface GasTankMatching {

    @Equals
    String getGas( );

    @Approximate( 0.000001d )
    double getVolume( );

    @Identical
    String getHazardLevel( );
}
