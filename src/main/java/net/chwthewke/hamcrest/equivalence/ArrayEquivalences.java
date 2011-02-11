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
 * {@link ArrayEquivalences} allows to adapt an equivalence on an {@link Iterable} into an equivalence that acts upon arrays
 * of the same component type. Additional overloads for arrays of primitive types are provided.
 */
public final class ArrayEquivalences {

    /**
     * Adapts an equivalence on {@link Iterable}&lt;T&gt; into an equivalence on <code>T[]</code>.
     * 
     * @param <T>
     *            The component type.
     * @param iterableEquivalence
     *            An equivalence on {@link Iterable}&lt;T&gt;.
     * @return The adapted equivalence on <code>T[]</code>.
     */
    public static <T> Equivalence<T[ ]> forArrays( final Equivalence<Iterable<? extends T>> iterableEquivalence ) {
        final Function<T[ ], Iterable<? extends T>> toList =
                new Function<T[ ], Iterable<? extends T>>( ) {
                    public Iterable<T> apply( final T[ ] input ) {
                        return newArrayList( input );
                    }
                };
        return new LiftedEquivalence<T[ ], Iterable<? extends T>>( "",
                iterableEquivalence, toList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Boolean&gt; into an equivalence on <code>boolean[]</code>.
     * 
     * @param booleanEquivalence
     *            An equivalence on {@link Iterable}&lt;Boolean&gt;
     * @return The adapted equivalence on <code>boolean[]</code>.
     */
    public static Equivalence<boolean[ ]> forBooleanArrays(
            final Equivalence<Iterable<? extends Boolean>> booleanEquivalence ) {
        final Function<boolean[ ], Iterable<? extends Boolean>> toBooleanList =
                new Function<boolean[ ], Iterable<? extends Boolean>>( ) {
                    public Iterable<? extends Boolean> apply( final boolean[ ] input ) {
                        return Booleans.asList( input );
                    }
                };
        return new LiftedEquivalence<boolean[ ], Iterable<? extends Boolean>>( "",
                booleanEquivalence, toBooleanList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Byte&gt; into an equivalence on <code>byte[]</code>.
     * 
     * @param byteEquivalence
     *            An equivalence on {@link Iterable}&lt;Byte&gt;
     * @return The adapted equivalence on <code>byte[]</code>.
     */
    public static Equivalence<byte[ ]> forByteArrays( final Equivalence<Iterable<? extends Byte>> byteEquivalence ) {
        final Function<byte[ ], Iterable<? extends Byte>> toByteList =
                new Function<byte[ ], Iterable<? extends Byte>>( ) {
                    public Iterable<? extends Byte> apply( final byte[ ] input ) {
                        return Bytes.asList( input );
                    }
                };
        return new LiftedEquivalence<byte[ ], Iterable<? extends Byte>>( "",
                byteEquivalence, toByteList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Character&gt; into an equivalence on <code>char[]</code>.
     * 
     * @param charEquivalence
     *            An equivalence on {@link Iterable}&lt;Character&gt;
     * @return The adapted equivalence on <code>char[]</code>.
     */
    public static Equivalence<char[ ]> forCharacterArrays(
            final Equivalence<Iterable<? extends Character>> charEquivalence ) {
        final Function<char[ ], Iterable<? extends Character>> toCharacterList =
                new Function<char[ ], Iterable<? extends Character>>( ) {
                    public Iterable<? extends Character> apply( final char[ ] input ) {
                        return Chars.asList( input );
                    }
                };
        return new LiftedEquivalence<char[ ], Iterable<? extends Character>>( "",
                charEquivalence, toCharacterList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Double&gt; into an equivalence on <code>double[]</code>.
     * 
     * @param doubleEquivalence
     *            An equivalence on {@link Iterable}&lt;Double&gt;
     * @return The adapted equivalence on <code>double[]</code>.
     */
    public static Equivalence<double[ ]> forDoubleArrays(
            final Equivalence<Iterable<? extends Double>> doubleEquivalence ) {
        final Function<double[ ], Iterable<? extends Double>> toDoubleList =
                new Function<double[ ], Iterable<? extends Double>>( ) {
                    public Iterable<? extends Double> apply( final double[ ] input ) {
                        return Doubles.asList( input );
                    }
                };
        return new LiftedEquivalence<double[ ], Iterable<? extends Double>>( "",
                doubleEquivalence, toDoubleList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Float&gt; into an equivalence on <code>float[]</code>.
     * 
     * @param floatEquivalence
     *            An equivalence on {@link Iterable}&lt;Float&gt;
     * @return The adapted equivalence on <code>float[]</code>.
     */
    public static Equivalence<float[ ]> forFloatArrays( final Equivalence<Iterable<? extends Float>> floatEquivalence ) {
        final Function<float[ ], Iterable<? extends Float>> toFloatList =
                new Function<float[ ], Iterable<? extends Float>>( ) {
                    public Iterable<? extends Float> apply( final float[ ] input ) {
                        return Floats.asList( input );
                    }
                };
        return new LiftedEquivalence<float[ ], Iterable<? extends Float>>( "",
                floatEquivalence, toFloatList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Integer&gt; into an equivalence on <code>int[]</code>.
     * 
     * @param intEquivalence
     *            An equivalence on {@link Iterable}&lt;Integer&gt;
     * @return The adapted equivalence on <code>int[]</code>.
     */
    public static Equivalence<int[ ]> forIntArrays( final Equivalence<Iterable<? extends Integer>> intEquivalence ) {
        final Function<int[ ], Iterable<? extends Integer>> toIntList =
                new Function<int[ ], Iterable<? extends Integer>>( ) {
                    public Iterable<? extends Integer> apply( final int[ ] input ) {
                        return Ints.asList( input );
                    }
                };
        return new LiftedEquivalence<int[ ], Iterable<? extends Integer>>( "",
                intEquivalence, toIntList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Long&gt; into an equivalence on <code>long[]</code>.
     * 
     * @param longEquivalence
     *            An equivalence on {@link Iterable}&lt;Long&gt;
     * @return The adapted equivalence on <code>long[]</code>.
     */
    public static Equivalence<long[ ]> forLongArrays( final Equivalence<Iterable<? extends Long>> longEquivalence ) {
        final Function<long[ ], Iterable<? extends Long>> toLongList =
                new Function<long[ ], Iterable<? extends Long>>( ) {
                    public Iterable<? extends Long> apply( final long[ ] input ) {
                        return Longs.asList( input );
                    }
                };
        return new LiftedEquivalence<long[ ], Iterable<? extends Long>>( "",
                longEquivalence, toLongList );
    }

    /**
     * Adapts an equivalence on {@link Iterable}&lt;Short&gt; into an equivalence on <code>short[]</code>.
     * 
     * @param shortEquivalence
     *            An equivalence on {@link Iterable}&lt;Short&gt;
     * @return The adapted equivalence on <code>short[]</code>.
     */
    public static Equivalence<short[ ]> forShortArrays( final Equivalence<Iterable<? extends Short>> shortEquivalence ) {
        final Function<short[ ], Iterable<? extends Short>> toShortList =
                new Function<short[ ], Iterable<? extends Short>>( ) {
                    public Iterable<? extends Short> apply( final short[ ] input ) {
                        return Shorts.asList( input );
                    }
                };
        return new LiftedEquivalence<short[ ], Iterable<? extends Short>>( "",
                shortEquivalence, toShortList );
    }

    private ArrayEquivalences( ) {
    }
}
