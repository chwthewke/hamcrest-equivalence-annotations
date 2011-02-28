package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Function;
import com.google.common.primitives.Booleans;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

public final class ArrayEquivalences2 {

    public static <T> Equivalence<T[ ]> arrayEquivalence( final Equivalence<T> equivalence, final boolean inOrder ) {
        final Function<T[ ], Iterable<? extends T>> toList = arrayToListFunction( );

        return liftToArray( equivalence, inOrder, toList );
    }

    public static Equivalence<boolean[ ]> booleanArrayEquivalence( final Equivalence<Boolean> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<boolean[ ], Iterable<? extends Boolean>>( ) {
            public Iterable<Boolean> apply( final boolean[ ] array ) {
                return Booleans.asList( array );
            }
        } );
    }

    public static Equivalence<byte[ ]> byteArrayEquivalence( final Equivalence<Byte> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<byte[ ], Iterable<? extends Byte>>( ) {
            public Iterable<Byte> apply( final byte[ ] array ) {
                return Bytes.asList( array );
            }
        } );
    }

    public static Equivalence<char[ ]> charArrayEquivalence( final Equivalence<Character> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<char[ ], Iterable<? extends Character>>( ) {
            public Iterable<Character> apply( final char[ ] array ) {
                return Chars.asList( array );
            }
        } );
    }

    public static Equivalence<double[ ]> doubleArrayEquivalence( final Equivalence<Double> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<double[ ], Iterable<? extends Double>>( ) {
            public Iterable<Double> apply( final double[ ] array ) {
                return Doubles.asList( array );
            }
        } );
    }

    public static Equivalence<float[ ]> floatArrayEquivalence( final Equivalence<Float> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<float[ ], Iterable<? extends Float>>( ) {
            public Iterable<Float> apply( final float[ ] array ) {
                return Floats.asList( array );
            }
        } );
    }

    public static Equivalence<int[ ]> intArrayEquivalence( final Equivalence<Integer> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<int[ ], Iterable<? extends Integer>>( ) {
            public Iterable<Integer> apply( final int[ ] array ) {
                return Ints.asList( array );
            }
        } );
    }

    public static Equivalence<long[ ]> longArrayEquivalence( final Equivalence<Long> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<long[ ], Iterable<? extends Long>>( ) {
            public Iterable<Long> apply( final long[ ] array ) {
                return Longs.asList( array );
            }
        } );
    }

    public static Equivalence<short[ ]> shortArrayEquivalence( final Equivalence<Short> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<short[ ], Iterable<? extends Short>>( ) {
            public Iterable<Short> apply( final short[ ] array ) {
                return Shorts.asList( array );
            }
        } );
    }

    private static <T, U> Equivalence<U> liftToArray( final Equivalence<T> equivalence, final boolean inOrder,
            final Function<U, Iterable<? extends T>> toList ) {
        return new LiftedEquivalence<U, Iterable<? extends T>>( "",
                new IterableEquivalence<T>( equivalence, inOrder ), toList );
    }

    private static <T> Function<T[ ], Iterable<? extends T>> arrayToListFunction( ) {
        return new Function<T[ ], Iterable<? extends T>>( ) {
            public Iterable<T> apply( final T[ ] input ) {
                return newArrayList( input );
            }
        };
    }

    private ArrayEquivalences2( ) {
    }
}
