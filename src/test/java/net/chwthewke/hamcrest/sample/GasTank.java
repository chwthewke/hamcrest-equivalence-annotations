/*
 * Copyright (c) 2010-2011, Thomas Dufour
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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
