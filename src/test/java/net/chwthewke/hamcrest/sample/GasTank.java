package net.chwthewke.hamcrest.sample;

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
            this.type = type;
        }

        @Override
        public String toString( ) {
            return type;
        }

        @Override
        public int hashCode( ) {
            final int prime = 31;
            int result = 1;
            result = prime * result + ( ( type == null ) ? 0 : type.hashCode( ) );
            return result;
        }

        @Override
        public boolean equals( final Object obj ) {
            if ( this == obj )
                return true;
            if ( obj == null )
                return false;
            if ( getClass( ) != obj.getClass( ) )
                return false;
            final Hazard other = (Hazard) obj;
            if ( type == null )
            {
                if ( other.type != null )
                    return false;
            }
            else if ( !type.equals( other.type ) )
                return false;
            return true;
        }

        private final String type;
    }
}
