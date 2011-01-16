package net.chwthewke.hamcrest.sample;

import net.chwthewke.hamcrest.annotations.ApproximateEquality;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.Identity;
import net.chwthewke.hamcrest.annotations.MatcherOf;

@MatcherOf( GasTank.class )
public interface GasTankMatching {

    @Equality
    String getGas( );

    @ApproximateEquality( tolerance = 0.000001d )
    double getVolume( );

    @Identity
    GasTank.Hazard getHazardLevel( );
}
