package net.chwthewke.hamcrest.annotations;

import net.chwthewke.hamcrest.MatcherSpecification;

public class BySpecificationTest {

    @MatcherOf( OuterMatched.class )
    public static interface OuterSpecification extends MatcherSpecification<OuterMatched> {
        @BySpecification( InnerSpecification.class )
        InnerMatched getValue( );
    }

    public static class OuterMatched {
        public OuterMatched( final InnerMatched value ) {
            this.value = value;
        }

        public InnerMatched getValue( ) {
            return value;
        }

        private final InnerMatched value;
    }

    @MatcherOf( InnerMatched.class )
    public static interface InnerSpecification extends MatcherSpecification<InnerMatched> {
        @Equality
        String getValue( );
    }

    public static class InnerMatched {
        public InnerMatched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        private final String value;
    }
}
