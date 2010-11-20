package net.chwthewke.hamcrest.annotations;

public class GasTank {

    public static final String LOW_HAZARD = "l";
    public static final String HIGH_HAZARD = "h";
    public static final String EXTREME_HAZARD = "x";

    public GasTank( final String gas, final double volume, final String hazardLevel ) {
        this.gas = gas;
        this.volume = volume;
        this.hazardLevel = hazardLevel;
    }

    public String getGas( ) {
        return gas;
    }

    public double getVolume( ) {
        return volume;
    }

    public String getHazardLevel( ) {
        return hazardLevel;
    }

    private final String gas;
    private final double volume;
    private final String hazardLevel;
}
