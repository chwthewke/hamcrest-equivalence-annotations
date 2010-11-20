package net.chwthewke.hamcrest.annotations;

public class GasTank {

    public GasTank( final String gas, final double volume, final Hazard hazardLevel ) {
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

    public Hazard getHazardLevel( ) {
        return hazardLevel;
    }

    private final String gas;
    private final double volume;
    private final Hazard hazardLevel;

    public static class Hazard {
        public static final Hazard LOW = new Hazard( "l" );
        public static final Hazard HIGH = new Hazard( "h" );
        public static final Hazard EXTREME = new Hazard( "x" );

        Hazard( final String type ) {
            super( );
            this.type = type;
        }

        @Override
        public String toString( ) {
            return type;
        }

        private final String type;
    }
}
