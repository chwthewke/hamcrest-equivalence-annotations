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

/**
 * {@link ArrayEquivalences} allows to adapt an equivalence on a given type into an equivalence that acts upon arrays
 * of that type. Additional overloads for arrays of primitive types are provided.
 */
public final class ArrayEquivalences {

    /**
     * Adapts an equivalence on <code>T</code> into an equivalence on <code>T[]</code>. Two arrays will be equivalent
     * by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param <T>
     *            The component type.
     * @param equivalence
     *            An equivalence on T.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>T[]</code>.
     */
    public static <T> Equivalence<T[ ]> arrayEquivalence( final Equivalence<? super T> equivalence,
            final boolean inOrder ) {
        final Function<T[ ], Iterable<? extends T>> toList = arrayToListFunction( );

        return liftToArray( equivalence, inOrder, toList );
    }

    /**
     * Adapts an equivalence on <code>Boolean</code> into an equivalence on <code>boolean[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Boolean.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>boolean[]</code>.
     */
    public static Equivalence<boolean[ ]> booleanArrayEquivalence( final Equivalence<? super Boolean> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<boolean[ ], Iterable<? extends Boolean>>( ) {
            public Iterable<Boolean> apply( final boolean[ ] array ) {
                return Booleans.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Byte</code> into an equivalence on <code>byte[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Byte.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>byte[]</code>.
     */
    public static Equivalence<byte[ ]> byteArrayEquivalence( final Equivalence<? super Byte> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<byte[ ], Iterable<? extends Byte>>( ) {
            public Iterable<Byte> apply( final byte[ ] array ) {
                return Bytes.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Character</code> into an equivalence on <code>char[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Character.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>char[]</code>.
     */
    public static Equivalence<char[ ]> charArrayEquivalence( final Equivalence<? super Character> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<char[ ], Iterable<? extends Character>>( ) {
            public Iterable<Character> apply( final char[ ] array ) {
                return Chars.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Double</code> into an equivalence on <code>double[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Double.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>double[]</code>.
     */
    public static Equivalence<double[ ]> doubleArrayEquivalence( final Equivalence<? super Double> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<double[ ], Iterable<? extends Double>>( ) {
            public Iterable<Double> apply( final double[ ] array ) {
                return Doubles.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Float</code> into an equivalence on <code>float[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Float.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>float[]</code>.
     */
    public static Equivalence<float[ ]> floatArrayEquivalence( final Equivalence<? super Float> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<float[ ], Iterable<? extends Float>>( ) {
            public Iterable<Float> apply( final float[ ] array ) {
                return Floats.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Integer</code> into an equivalence on <code>int[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Integer.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>int[]</code>.
     */
    public static Equivalence<int[ ]> intArrayEquivalence( final Equivalence<? super Integer> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<int[ ], Iterable<? extends Integer>>( ) {
            public Iterable<Integer> apply( final int[ ] array ) {
                return Ints.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Long</code> into an equivalence on <code>long[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Long.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>long[]</code>.
     */
    public static Equivalence<long[ ]> longArrayEquivalence( final Equivalence<? super Long> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<long[ ], Iterable<? extends Long>>( ) {
            public Iterable<Long> apply( final long[ ] array ) {
                return Longs.asList( array );
            }
        } );
    }

    /**
     * Adapts an equivalence on <code>Short</code> into an equivalence on <code>short[]</code>. Two arrays will be
     * equivalent by the returned equivalence iff there exists a 1-to-1 mapping between their respective
     * elements such that each pair of elements in the mapping are equivalent according to the provided
     * <code>equivalence</code>.
     * 
     * @param equivalence
     *            An equivalence on Short.
     * @param inOrder
     *            When <code>true</code>, imposes the additional restriction that the above-mentioned mapping must
     *            respect array order.
     * @return The adapted equivalence on <code>short[]</code>.
     */
    public static Equivalence<short[ ]> shortArrayEquivalence( final Equivalence<? super Short> equivalence,
            final boolean inOrder ) {
        return liftToArray( equivalence, inOrder, new Function<short[ ], Iterable<? extends Short>>( ) {
            public Iterable<Short> apply( final short[ ] array ) {
                return Shorts.asList( array );
            }
        } );
    }

    private static <T, U> Equivalence<U> liftToArray( final Equivalence<? super T> equivalence, final boolean inOrder,
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

    private ArrayEquivalences( ) {
    }
}
