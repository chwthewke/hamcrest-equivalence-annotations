package net.chwthewke.hamcrest.equivalence;

import com.google.common.base.Function;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public final class PrimitiveArrayEquivalences {

    public static Equivalence<boolean[ ]> forBooleanArrays(
            final Equivalence<Iterable<? extends Boolean>> booleanEquivalence ) {
        final Function<boolean[ ], Iterable<? extends Boolean>> toBooleanList = new Function<boolean[ ], Iterable<? extends Boolean>>( ) {
            public Iterable<? extends Boolean> apply( final boolean[ ] input ) {
                return Booleans.asList( input );
            }
        };
        return new LiftedEquivalence<boolean[ ], Iterable<? extends Boolean>>( "",
                booleanEquivalence, toBooleanList );
    }

    public static Equivalence<byte[ ]> forByteArrays( final Equivalence<Iterable<? extends Byte>> byteEquivalence ) {
        final Function<byte[ ], Iterable<? extends Byte>> toByteList = new Function<byte[ ], Iterable<? extends Byte>>( ) {
            public Iterable<? extends Byte> apply( final byte[ ] input ) {
                return Bytes.asList( input );
            }
        };
        return new LiftedEquivalence<byte[ ], Iterable<? extends Byte>>( "",
                byteEquivalence, toByteList );
    }

    public static Equivalence<char[ ]> forCharacterArrays(
            final Equivalence<Iterable<? extends Character>> charEquivalence ) {
        final Function<char[ ], Iterable<? extends Character>> toCharacterList = new Function<char[ ], Iterable<? extends Character>>( ) {
            public Iterable<? extends Character> apply( final char[ ] input ) {
                return Chars.asList( input );
            }
        };
        return new LiftedEquivalence<char[ ], Iterable<? extends Character>>( "",
                charEquivalence, toCharacterList );
    }

    public static Equivalence<double[ ]> forDoubleArrays(
            final Equivalence<Iterable<? extends Double>> doubleEquivalence ) {
        final Function<double[ ], Iterable<? extends Double>> toDoubleList = new Function<double[ ], Iterable<? extends Double>>( ) {
            public Iterable<? extends Double> apply( final double[ ] input ) {
                return Doubles.asList( input );
            }
        };
        return new LiftedEquivalence<double[ ], Iterable<? extends Double>>( "",
                doubleEquivalence, toDoubleList );
    }

    public static Equivalence<float[ ]> forFloatArrays( final Equivalence<Iterable<? extends Float>> floatEquivalence ) {
        final Function<float[ ], Iterable<? extends Float>> toFloatList = new Function<float[ ], Iterable<? extends Float>>( ) {
            public Iterable<? extends Float> apply( final float[ ] input ) {
                return Floats.asList( input );
            }
        };
        return new LiftedEquivalence<float[ ], Iterable<? extends Float>>( "",
                floatEquivalence, toFloatList );
    }

    public static Equivalence<int[ ]> forIntArrays( final Equivalence<Iterable<? extends Integer>> intEquivalence ) {
        final Function<int[ ], Iterable<? extends Integer>> toIntList = new Function<int[ ], Iterable<? extends Integer>>( ) {
            public Iterable<? extends Integer> apply( final int[ ] input ) {
                return Ints.asList( input );
            }
        };
        return new LiftedEquivalence<int[ ], Iterable<? extends Integer>>( "",
                intEquivalence, toIntList );
    }

    public static Equivalence<long[ ]> forLongArrays( final Equivalence<Iterable<? extends Long>> longEquivalence ) {
        final Function<long[ ], Iterable<? extends Long>> toLongList = new Function<long[ ], Iterable<? extends Long>>( ) {
            public Iterable<? extends Long> apply( final long[ ] input ) {
                return Longs.asList( input );
            }
        };
        return new LiftedEquivalence<long[ ], Iterable<? extends Long>>( "",
                longEquivalence, toLongList );
    }

    public static Equivalence<short[ ]> forShortArrays( final Equivalence<Iterable<? extends Short>> shortEquivalence ) {
        final Function<short[ ], Iterable<? extends Short>> toShortList = new Function<short[ ], Iterable<? extends Short>>( ) {
            public Iterable<? extends Short> apply( final short[ ] input ) {
                return Shorts.asList( input );
            }
        };
        return new LiftedEquivalence<short[ ], Iterable<? extends Short>>( "",
                shortEquivalence, toShortList );
    }

    private PrimitiveArrayEquivalences( ) {
    }
}
