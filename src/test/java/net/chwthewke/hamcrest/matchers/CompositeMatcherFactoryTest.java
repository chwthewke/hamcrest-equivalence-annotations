package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.AnnotationMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.MatcherOf;

import org.junit.Test;

public class CompositeMatcherFactoryTest {

    @Test
    public void matchingInterfaceTargetsMissingMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithMisnamedMethod.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched lacks the property " +
                        "method 'value()' present on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithMisnamedMethod." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasNonPropertyMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithNonPropertyMethod.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The method public abstract java.lang.String " +
                        "net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest" +
                        "$SpecificationWithNonPropertyMethod.method(java.lang.Object) " +
                        "in specification net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithNonPropertyMethod " +
                        "has parameters." ) ) );
        }
    }

    @Test
    public void matchingInterfaceTargetsMethodWithArguments( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithArgumentsMismatch.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched lacks the property method " +
                        "'compute()' present on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithArgumentsMismatch." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasIncompatibleReturnType( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithReturnTypeMismatch.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The property 'getValue()' on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched has return type " +
                        "java.lang.String which is not assignable to java.lang.Integer " +
                        "as specified on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithReturnTypeMismatch." ) ) );
        }
    }

    @Test
    public void matchingInterfaceTargetsPrivateProperty( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            asSpecifiedBy(
                SpecificationWithPrivateProperty.class,
                Matched.class )
                .equivalentTo( new Matched( "test" ) );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The matched class net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$Matched lacks the property method " +
                        "'getId()' present on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithPrivateProperty." ) ) );

        }
    }

    public static class Matched {

        public Matched( final String value ) {
            this.value = value;
        }

        public String getValue( ) {
            return value;
        }

        public String compute( final String foo ) {
            return value + foo;
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithMisnamedMethod {
        @Equality
        String value( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithNonPropertyMethod {
        @Equality
        String method( Object input );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithArgumentsMismatch {
        @Equality
        String compute( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithReturnTypeMismatch {
        @Equality
        Integer getValue( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithPrivateProperty {
        @Equality
        int getId( );
    }

}
