package net.chwthewke.hamcrest.equivalence;

import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_CASE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_LEADING_WHITESPACE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_TRAILING_WHITESPACE;
import static net.chwthewke.hamcrest.equivalence.TextEquivalenceOption.IGNORE_WHITESPACE;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Sets;

/**
 * The {@link EqualityEquivalence} class defines equivalence as equality on {@link String}s
 * starting with strict equality that can be relaxed by a number of options defined by the {@link TextEquivalenceOption} enum.
 */
public final class TextEquivalence {

    public static Equivalence<String> textEquivalenceWith( final TextEquivalenceOption... options ) {
        final Set<TextEquivalenceOption> optionSet = newHashSet( options );
        final Equivalence<String> coreEquivalence = new TextEquivalenceCore( intersection( coreOptions, optionSet ) );

        final Set<TextEquivalenceOption> nonCoreOptions = Sets.difference( optionSet, coreOptions );

        if ( nonCoreOptions.isEmpty( ) )
            return coreEquivalence;

        final NamedProjection namedProjection = getNamedProjection( nonCoreOptions );

        return new LiftedEquivalence<String, String>( namedProjection.name, coreEquivalence, namedProjection.projection );
    }

    private static NamedProjection getNamedProjection( final Set<TextEquivalenceOption> nonCoreOptions ) {

        if ( nonCoreOptions.contains( IGNORE_WHITESPACE ) )
            return new NamedProjection( "ignoring whitespace",
                new Function<String, String>( ) {
                    public String apply( final String input ) {
                        return input.replaceAll( "\\s", "" );
                    }
                } );

        final boolean leftTrimmed = nonCoreOptions.contains( IGNORE_LEADING_WHITESPACE );
        final boolean rightTrimmed = nonCoreOptions.contains( IGNORE_TRAILING_WHITESPACE );

        if ( leftTrimmed && rightTrimmed )
            return new NamedProjection( "trimmed", new Function<String, String>( ) {
                public String apply( final String input ) {
                    return input.trim( );
                }
            } );

        if ( leftTrimmed )
            return new NamedProjection( "left-trimmed", new Function<String, String>( ) {
                public String apply( final String input ) {
                    return input.replaceAll( "^\\s+", "" );
                }
            } );

        if ( rightTrimmed )
            return new NamedProjection( "right-trimmed", new Function<String, String>( ) {
                public String apply( final String input ) {
                    return input.replaceAll( "\\s+$", "" );
                }
            } );

        throw new IllegalStateException( "cannot call getNamedProjection( ) on an empty set." );
    }

    private TextEquivalence( ) {
    }

    private static EnumSet<TextEquivalenceOption> coreOptions = EnumSet.of( IGNORE_CASE );

    private static class NamedProjection { // NOCHECK HideUtilityClassConstructor
        NamedProjection( final String name, final Function<String, String> projection ) {
            this.name = name;
            this.projection = projection;
        }

        private final String name;
        private final Function<String, String> projection;
    }
}
