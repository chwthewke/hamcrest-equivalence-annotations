package net.chwthewke.hamcrest.annotations;

import static net.chwthewke.hamcrest.annotations.CompositeMatcherFactory.matcherBySpecification;
import net.chwthewke.hamcrest.annotations.declarations.ReflectivePrimitiveMatch;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MatchPrimitivePropertiesTest {

    @Test
    public void createMatcherOfPrimitiveProperties( ) throws Exception {
        // Setup
        // Exercise
        matcherBySpecification(
            ReflectivePrimitiveMatch.Matched.class,
            ReflectivePrimitiveMatch.MatcherSpecification.class )
            .of( new ReflectivePrimitiveMatch.Matched( 1, 4 ) );
        // Verify

    }

}
