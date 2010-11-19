package net.chwthewke.hamcrest.annotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BasicAnnotatedMatcherTest {

    private Matcher<BasicMatchable> basicSpecificationMatcher;

    @Before
    public void setupMatcher( ) {
    }

    @Test
    public void descriptionIncludesMatchedClassNameAndProperties( ) throws Exception {
        // Setup
        basicSpecificationMatcher =
                new AnnotationMatcher<BasicMatchable>(
                    new BasicMatchable( 1L ), BasicMatchable.class, BasicMatcherSpec.class );

        final StringDescription description = new StringDescription( );
        // Exercise
        basicSpecificationMatcher.describeTo( description );
        // Verify
        final String expected = "a BasicMatchable with getValue=<1L>";
        assertThat( description.toString( ), is( equalTo( expected ) ) );
    }

    @Test
    public void basicMatcherSpecDifferentiatesOnValue( ) throws Exception {
        // Setup
        final BasicMatchable first = new BasicMatchable( 1L );
        final BasicMatchable second = new BasicMatchable( 4D );
        basicSpecificationMatcher =
                new AnnotationMatcher<BasicMatchable>( first, BasicMatchable.class, BasicMatcherSpec.class );
        // Exercise
        final boolean match = basicSpecificationMatcher.matches( second );
        // Verify
        assertThat( match, is( false ) );

    }

    @Test
    public void basicMatcherSpecRespectsIdentity( ) throws Exception {
        // Setup
        final Object value = new Object( );
        final BasicMatchable first = new BasicMatchable( value );
        final BasicMatchable second = new BasicMatchable( value );
        basicSpecificationMatcher =
                new AnnotationMatcher<BasicMatchable>( first, BasicMatchable.class, BasicMatcherSpec.class );
        // Exercise
        final boolean match = basicSpecificationMatcher.matches( second );
        // Verify
        assertThat( match, is( true ) );
    }

    @Test
    @Ignore
    public void basicMatcherSpecMismatchDescription( ) {
        // Setup
        final BasicMatchable first = new BasicMatchable( 1L );
        final BasicMatchable second = new BasicMatchable( 4D );
        basicSpecificationMatcher =
                new AnnotationMatcher<BasicMatchable>( first, BasicMatchable.class, BasicMatcherSpec.class );
        final StringDescription description = new StringDescription( );
        // Exercise
        basicSpecificationMatcher.matches( second );
        basicSpecificationMatcher.describeTo( description );
        // Verify
        assertThat( description.toString( ), is( equalTo( null ) ) );
    }
}
