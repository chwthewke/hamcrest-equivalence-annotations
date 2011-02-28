package net.chwthewke.hamcrest.equivalence;

import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.arrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.booleanArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.byteArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.charArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.doubleArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.floatArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.intArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.longArrayEquivalence;
import static net.chwthewke.hamcrest.equivalence.ArrayEquivalences2.shortArrayEquivalence;
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
                arrayEquivalence( textEquivalenceWith( IGNORE_CASE ), true );
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
                booleanArrayEquivalence( new EqualityEquivalence<Boolean>( ), false );
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
                byteArrayEquivalence( new EqualityEquivalence<Byte>( ), true );
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
                charArrayEquivalence( new EqualityEquivalence<Character>( ), true );
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
                doubleArrayEquivalence( new ApproximateEqualityEquivalence( 0.001d ), true );
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
                floatArrayEquivalence( new ApproximateEqualityEquivalence( 0.001d ), true );
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
                intArrayEquivalence( new EqualityEquivalence<Integer>( ), true );
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
                longArrayEquivalence( new EqualityEquivalence<Long>( ), true );
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
                shortArrayEquivalence( new EqualityEquivalence<Short>( ), true );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x12, 0x51, -0x44 }, new short[ ] { 0x51, 0x12 } ) );
    }

}
