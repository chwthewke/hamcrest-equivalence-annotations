package net.chwthewke.hamcrest.annotations.att2;

import static net.chwthewke.hamcrest.annotations.att2.CompositeMatcherFactory.matcherBySpecification;
import net.chwthewke.hamcrest.annotations.declarations.ReflectivePrimitiveMatch;
import net.chwthewke.hamcrest.annotations.declarations.ReflectivePrimitiveMatch.Matched;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MatchPrimitivePropertiesTest {

    @Test
    public void createMatcherOfPrimitiveProperties( ) throws Exception {
        // Setup
        // Exercise
        final Matcher<Matched> matcher = matcherBySpecification(
            ReflectivePrimitiveMatch.Matched.class,
            ReflectivePrimitiveMatch.MatcherSpecification.class )
            .of( new ReflectivePrimitiveMatch.Matched( 1, 4 ) );
        // Verify

    }

}
