package net.chwthewke.hamcrest.matchers;

import static com.google.common.collect.Lists.newArrayList;
import static net.chwthewke.hamcrest.matchers.EquivalenceAnnotationProcessor.annotationProcessorFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import net.chwthewke.hamcrest.equivalence.Equivalence;
import net.chwthewke.hamcrest.sut.classes.WithPublicProperty;
import net.chwthewke.hamcrest.sut.specs.EqualityOnString;
import net.chwthewke.hamcrest.sut.specs.IdentityOnPrimitive;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

public class CompositeMatcherTest {

    private Equivalence<WithPublicProperty> equalityOnString;
    private Equivalence<WithPublicProperty> identityOnPrimitive;

    @Before
    public void createEquivalences( ) throws Exception {
        equalityOnString = annotationProcessorFor( WithPublicProperty.class,
            EqualityOnString.class.getMethod( "getValue" ),
            WithPublicProperty.class.getMethod( "getValue" ) )
            .processEquivalenceSpecification( );
        identityOnPrimitive = annotationProcessorFor( WithPublicProperty.class,
            IdentityOnPrimitive.class.getMethod( "getIntValue" ),
            WithPublicProperty.class.getMethod( "getIntValue" ) )
            .processEquivalenceSpecification( );
    }

    @Test
    public void matcherDescribesAllExpectations( ) throws Exception {
        // Setup
        @SuppressWarnings( "unchecked" )
        final Matcher<WithPublicProperty> matcher =
                new CompositeMatcher<WithPublicProperty>( WithPublicProperty.class,
                    newArrayList( equalityOnString, identityOnPrimitive ),
                    new WithPublicProperty( "123" ) );
        final Description description = new StringDescription( );
        // Exercise
        matcher.describeTo( description );
        // Verify
        assertThat(
            description.toString( ),
            is( equalTo( "a WithPublicProperty with getValue()=\"123\", getIntValue()=<123>" ) ) );
    }

    @Test
    public void matcherMatchSucceeds( ) throws Exception {
        // Setup
        @SuppressWarnings( "unchecked" )
        final Matcher<WithPublicProperty> matcher =
                new CompositeMatcher<WithPublicProperty>( WithPublicProperty.class,
                    newArrayList( equalityOnString ),
                    new WithPublicProperty( "123" ) );
        // Exercise
        // Verify
        assertThat( matcher.matches( new WithPublicProperty( "123" ) ), is( true ) );
    }

    @Test
    public void matcherMatchFailsWithDescription( ) throws Exception {
        // Setup
        @SuppressWarnings( "unchecked" )
        final Matcher<WithPublicProperty> matcher =
                new CompositeMatcher<WithPublicProperty>( WithPublicProperty.class,
                    newArrayList( equalityOnString ),
                    new WithPublicProperty( "123" ) );
        final Description description = new StringDescription( );
        final WithPublicProperty actual = new WithPublicProperty( "456" );
        // Exercise
        // Verify
        assertThat( matcher.matches( actual ), is( false ) );
        matcher.describeMismatch( actual, description );
        assertThat( description.toString( ), is( equalTo( "getValue() was \"456\"" ) ) );
    }

    @Test
    public void matcherMatchFailsWithDescriptionOfFirstFailureOnly( ) throws Exception {
        // Setup
        @SuppressWarnings( "unchecked" )
        final Matcher<WithPublicProperty> matcher =
                new CompositeMatcher<WithPublicProperty>( WithPublicProperty.class,
                    newArrayList( equalityOnString, identityOnPrimitive ),
                    new WithPublicProperty( "123" ) );
        final Description description = new StringDescription( );
        final WithPublicProperty actual = new WithPublicProperty( "456" );
        // Exercise
        // Verify
        assertThat( matcher.matches( actual ), is( false ) );
        matcher.describeMismatch( actual, description );
        assertThat( description.toString( ), is( equalTo( "getValue() was \"456\"" ) ) );
    }
}
