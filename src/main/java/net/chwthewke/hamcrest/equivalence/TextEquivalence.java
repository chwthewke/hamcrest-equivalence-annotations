package net.chwthewke.hamcrest.equivalence;

public final class TextEquivalence {

    public static Equivalence<String> textEquivalenceWith( final TextEquivalenceOption... options ) {
        return new TextEquivalenceCore( options );
    }

    private TextEquivalence( ) {
    }
}
