package net.chwthewke.hamcrest.annotations;

public class GasTank {

    public GasTank( final String gas, final double volume ) {
        this.gas = gas;
        this.volume = volume;
    }

    public String getGas( ) {
        return gas;
    }

    public double getVolume( ) {
        return volume;
    }

    private final String gas;
    private final double volume;
}
