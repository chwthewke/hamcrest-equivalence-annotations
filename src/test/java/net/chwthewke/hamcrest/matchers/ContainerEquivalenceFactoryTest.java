package net.chwthewke.hamcrest.matchers;

import static com.google.common.collect.Lists.newArrayList;
import static net.chwthewke.hamcrest.MatcherUtils.describe;
import static net.chwthewke.hamcrest.MatcherUtils.describeMismatch;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.equates;
import static net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers.separates;
import static net.chwthewke.hamcrest.equivalence.TextEquivalence.textEquivalenceWith;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Collections;

import net.chwthewke.hamcrest.equivalence.ApproximateEqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.EqualityEquivalence;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.equivalence.EquivalenceClassMatchers;
import net.chwthewke.hamcrest.equivalence.TextEquivalence;
import net.chwthewke.hamcrest.equivalence.TextEquivalenceOption;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

public class ContainerEquivalenceFactoryTest {

    private ContainerEquivalenceFactory equivalenceFactory;

    @Before
    public void setupContainerEquivalenceFactory( ) {
        equivalenceFactory = new ContainerEquivalenceFactory( );
    }

    @Test
    public void inOrderStringEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, true );
        // Verify
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
                newArrayList( "abc", "def" ),
                newArrayList( "ABC", "Def" ) ) );
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>separates(
                newArrayList( "abc", "def" ),
                newArrayList( "abc", "de f" ),
                newArrayList( "Def", "ABC" ) ) );
    }

    @Test
    public void describeInOrderStringEquivalenceMatcher( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, true );
        // Verify
        final String description = describe( equivalence.equivalentTo( newArrayList( "Abc", "def" ) ) );
        assertThat( description, is( equalTo( "iterable containing " +
                "[equalToIgnoringCase(\"Abc\"), equalToIgnoringCase(\"def\")]" ) ) );
    }

    @Test
    public void inOrderEquivalenceWithEmptyIterable( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, true );
        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
            Collections.<String>emptyList( ), Collections.<String>emptyList( ) ) );
    }

    @Test
    public void inAnyOrderEquivalenceWithEmptyIterable( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, false );
        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
            Collections.<String>emptyList( ), Collections.<String>emptyList( ) ) );
    }

    @Test
    public void inOrderStringEquivalenceMismatch( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, true );
        final Matcher<Iterable<? extends String>> matcher = equivalence.equivalentTo( newArrayList( "ABC", "Def" ) );
        // Verify
        final String mismatchDescription = describeMismatch( matcher, newArrayList( "ABC", "D ef" ) );
        assertThat( mismatchDescription, is( equalTo( "item 1: was D ef" ) ) );
    }

    @Test
    public void inAnyOrderStringEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, false );
        // Verify
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>equates(
                newArrayList( "abc", "def" ),
                newArrayList( "ABC", "Def" ),
                newArrayList( "Def", "ABC" ) ) );
        assertThat(
            equivalence, EquivalenceClassMatchers.<Iterable<? extends String>>separates(
                newArrayList( "abc", "def" ),
                newArrayList( "abc", "de f" ) ) );
    }

    @Test
    public void describeInAnyOrderStringEquivalenceMatcher( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, false );
        // Verify
        final String description = describe( equivalence.equivalentTo( newArrayList( "Abc", "def" ) ) );
        assertThat( description, is( equalTo( "iterable over " +
                "[equalToIgnoringCase(\"Abc\"), equalToIgnoringCase(\"def\")] " +
                "in any order" ) ) );
    }

    @Test
    public void inAnyOrderStringEquivalenceMismatch( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<Iterable<? extends String>> equivalence = equivalenceFactory
            .iterableEquivalence( componentEquivalence, false );
        final Matcher<Iterable<? extends String>> matcher = equivalence.equivalentTo( newArrayList( "ABC", "Def" ) );
        // Verify
        final String mismatchDescription = describeMismatch( matcher, newArrayList( "Def", "AB C" ) );
        assertThat( mismatchDescription, is( equalTo( "Not matched: \"AB C\"" ) ) );
    }

    @Test
    public void testStringArrayInOrderEquivalence( ) throws Exception {
        // Setup
        final Equivalence<String[ ]> equivalence = equivalenceFactory
            .arrayEquivalence( textEquivalenceWith( IGNORE_CASE ), true );
        // Exercise
        // Verify
        assertThat( equivalence,
            equates( new String[ ] { "abc", "def" }, new String[ ] { "ABC", "DEF" } ) );
        assertThat( equivalence,
            separates(
                new String[ ] { "abc", "def" }, new String[ ] { "ABC" }, new String[ ] { "ABC", "deg" } ) );
    }

    @Test
    public void inOrderEquivalenceWithEmptyArray( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<String[ ]> equivalence = equivalenceFactory
            .arrayEquivalence( componentEquivalence, true );
        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<String[ ]>equates(
            new String[ 0 ], new String[ 0 ] ) );
    }

    @Test
    public void inAnyOrderEquivalenceWithEmptyArray( ) throws Exception {
        // Setup
        final Equivalence<String> componentEquivalence =
                TextEquivalence.textEquivalenceWith( TextEquivalenceOption.IGNORE_CASE );
        // Exercise
        final Equivalence<String[ ]> equivalence = equivalenceFactory
            .arrayEquivalence( componentEquivalence, false );
        // Verify
        assertThat( equivalence, EquivalenceClassMatchers.<String[ ]>equates(
            new String[ 0 ], new String[ 0 ] ) );
    }

    @Test
    public void testBooleanArrayInAnyOrder( ) throws Exception {
        // Setup
        final Equivalence<boolean[ ]> equivalence = equivalenceFactory
            .booleanArrayEquivalence( new EqualityEquivalence<Boolean>( ), false );
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
        final Equivalence<byte[ ]> equivalence = equivalenceFactory
            .byteArrayEquivalence( new EqualityEquivalence<Byte>( ), true );
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
        final Equivalence<char[ ]> equivalence = equivalenceFactory
            .charArrayEquivalence( new EqualityEquivalence<Character>( ), true );
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
        final Equivalence<double[ ]> equivalence = equivalenceFactory
            .doubleArrayEquivalence( new ApproximateEqualityEquivalence( 0.001d ), true );
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
        final Equivalence<float[ ]> equivalence = equivalenceFactory
            .floatArrayEquivalence( new ApproximateEqualityEquivalence( 0.001d ), true );
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
        final Equivalence<int[ ]> equivalence = equivalenceFactory
            .intArrayEquivalence( new EqualityEquivalence<Integer>( ), true );
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
        final Equivalence<long[ ]> equivalence = equivalenceFactory
            .longArrayEquivalence( new EqualityEquivalence<Long>( ), true );
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
        final Equivalence<short[ ]> equivalence = equivalenceFactory
            .shortArrayEquivalence( new EqualityEquivalence<Short>( ), true );
        // Exercise
        // Verify
        assertThat( equivalence, equates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x51, 0x12, -0x44 } ) );
        assertThat( equivalence, separates( new short[ ] { 0x51, 0x12, -0x44 },
            new short[ ] { 0x12, 0x51, -0x44 }, new short[ ] { 0x51, 0x12 } ) );
    }

}
