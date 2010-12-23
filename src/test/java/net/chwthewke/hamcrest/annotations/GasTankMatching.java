package net.chwthewke.hamcrest.annotations;

@MatcherOf( GasTank.class )
public interface GasTankMatching {

    @Equality
    String getGas( );

    @ApproximateEquality( 0.000001d )
    double getVolume( );

    @Identity
    GasTank.Hazard getHazardLevel( );
}
