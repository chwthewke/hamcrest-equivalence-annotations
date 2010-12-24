package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.matchers.AnnotationMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.atomic.AtomicReference;

import net.chwthewke.hamcrest.MatcherSpecification;

import org.junit.Ignore;
import org.junit.Test;

public class IdentityMatchersTest {

    @Test
    public void identicalReferencesMatch( ) throws Exception {
        // Setup
        final AtomicReference<Integer> ref = new AtomicReference<Integer>( 12 );
        final Matched first = new Matched( ref );
        final Matched second = new Matched( ref );
        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    @Ignore
    public void equalButNotIdenticalReferencesDoNotMatch( ) throws Exception {
        // Setup
        final Matched first = new Matched( new AtomicReference<Integer>( 12 ) );
        final Matched second = new Matched( new AtomicReference<Integer>( 12 ) );

        assertThat( first.equals( second ), is( true ) );

        // Exercise
        final boolean match = asSpecifiedBy( MatchingSpecification.class )
            .equivalentTo( first )
            .matches( second );
        // Verify
        assertThat( match, is( false ) );
    }

    public static class Matched {
        public Matched( final AtomicReference<Integer> value ) {
            this.value = value;
        }

        public AtomicReference<Integer> getValue( ) {
            return value;
        }

        private final AtomicReference<Integer> value;
    }

    @MatcherOf( Matched.class )
    public static interface MatchingSpecification extends MatcherSpecification<Matched> {
        @Identity
        AtomicReference<Integer> getValue( );
    }
}
