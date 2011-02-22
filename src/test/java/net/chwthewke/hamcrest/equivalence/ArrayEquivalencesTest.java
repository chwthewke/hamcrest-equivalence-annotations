package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forBooleanArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forByteArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forCharacterArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forDoubleArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forFloatArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forIntegerArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forLongArrays;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences.forShortArrays;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalence.textEquivalenceWith;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ArrayEquivalencesTest {

    @Test
    public void testStringArrayInOrderEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String[ ]> equivalence =
                forArrays( new IterableEquivalence<String>( textEquivalenceWith( IGNORE_CASE ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence,
            equates( new String[ ] { "abc", "def" }, new String[ ] { "ABC", "DEF" } ) );
        assertThat( equivalence,
            separates(
                new String[ ] { "abc", "def" }, new String[ ] { "ABC" }, new String[ ] { "ABC", "deg" } ) );
    }

    @Test
    public void testBooleanArrayInAnyOrder( ) throws Exception {
        // Setup
        final Equivalence<boolean[ ]> equivalence =
                forBooleanArrays( new IterableEquivalence<Boolean>( new EqualityEquivalence<Boolean>( ), false ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new boolean[ ] { true, false, false },
            new boolean[ ] { false, true, false }, new boolean[ ] { false, false, true }
            ) );
        assertThat( equivalence, separates( new boolean[ ] { true, false, false },
            new boolean[ ] { true, true, false }, new boolean[ ] { true, false } ) );
    }

    @Test
    public void testByteArrayInOrderTest( ) throws Exception {
        // Setup
        final Equivalence<byte[ ]> equivalence =
                forByteArrays( new IterableEquivalence<Byte>( new EqualityEquivalence<Byte>( ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new byte[ ] { 0x51, 0x12, -0x44 },
            new byte[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new byte[ ] { 0x51, 0x12, -0x44 },
            new byte[ ] { 0x12, 0x51, -0x44 }, new byte[ ] { 0x51, 0x12 } ) );
    }

    @Test
    public void testCharArrayInOrderTest( ) throws Exception {
        // Setup
        final Equivalence<char[ ]> equivalence =
                forCharacterArrays( new IterableEquivalence<Character>( new EqualityEquivalence<Character>( ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new char[ ] { 'A', 'B', 'þ' },
            new char[ ] { 'A', 'B', 'þ' } ) );
        assertThat( equivalence, separates( new char[ ] { 'A', 'B', 'þ' },
            new char[ ] { 'A', 'C', 'þ' }, new char[ ] { 'A', 'B' } ) );
    }

    @Test
    public void testDoubleArrayInOrderApproxTest( ) throws Exception {
        // Setup
        final Equivalence<double[ ]> equivalence =
                forDoubleArrays( new IterableEquivalence<Double>(
                    Transforms.<Number, Double>narrow( new ApproximateEqualityEquivalence( 0.001d ) ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new double[ ] { 1d, 4d, -5d },
            new double[ ] { 0.9999d, 4d, -5.0009d } ) );
        assertThat( equivalence, separates( new double[ ] { 1d, 4d, -5d },
            new double[ ] { 1d, 4d, -5.01d }, new double[ ] { 2d, 4d, -5d }, new double[ ] { 1d, -5d, 4d } ) );
    }

    @Test
    public void testFloatArrayInOrderApproxTest( ) throws Exception {
        // Setup
        final Equivalence<float[ ]> equivalence =
                forFloatArrays( new IterableEquivalence<Float>(
                    Transforms.<Number, Float>narrow( new ApproximateEqualityEquivalence( 0.001d ) ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new float[ ] { 1f, 4f, -5f },
            new float[ ] { 0.9999f, 4f, -5.0009f } ) );
        assertThat( equivalence, separates( new float[ ] { 1f, 4f, -5f },
            new float[ ] { 1f, 4f, -5.01f }, new float[ ] { 2f, 4f, -5f }, new float[ ] { 1f, -5f, 4f } ) );
    }

    @Test
    public void testIntArrayInOrderTest( ) throws Exception {
        // Setup
        final Equivalence<int[ ]> equivalence =
                forIntegerArrays( new IterableEquivalence<Integer>( new EqualityEquivalence<Integer>( ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new int[ ] { 0x51, 0x12, -0x44 },
            new int[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new int[ ] { 0x51, 0x12, -0x44 },
            new int[ ] { 0x12, 0x51, -0x44 }, new int[ ] { 0x51, 0x12 } ) );
    }

    @Test
    public void testLongArrayInOrderTest( ) throws Exception {
        // Setup
        final Equivalence<long[ ]> equivalence =
                forLongArrays( new IterableEquivalence<Long>( new EqualityEquivalence<Long>( ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new long[ ] { 0x51, 0x12, -0x44 },
            new long[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new long[ ] { 0x51, 0x12, -0x44 },
            new long[ ] { 0x12, 0x51, -0x44 }, new long[ ] { 0x51, 0x12 } ) );
    }

    @Test
    public void testShortArrayInOrderTest( ) throws Exception {
        // Setup
        final Equivalence<short[ ]> equivalence =
                forShortArrays( new IterableEquivalence<Short>( new EqualityEquivalence<Short>( ), true ) );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x12, 0x51, -0x44 }, new short[ ] { 0x51, 0x12 } ) );
    }

}
