package net.chwthewke.hamcrest.matchers;

import static net.chwthewke.hamcrest.matchers.AnnotationMatchers.asSpecifiedBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import net.chwthewke.hamcrest.MatcherFactory;
import net.chwthewke.hamcrest.MatcherSpecification;
import net.chwthewke.hamcrest.annotations.Equality;
import net.chwthewke.hamcrest.annotations.MatcherOf;

import org.junit.Test;

public class CompositeMatcherFactoryTest {

    @Test
    public void matchingSpecificationNotAnInterface( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationNotAnInterface.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationNotAnInterface must be an interface." ) ) );
        }
    }

    @Test
    public void matchingSpecificationNonPublicInterface( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                NonPublicInterface.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$NonPublicInterface must have public visibility." ) ) );
        }
    }

    @Test
    public void matchingSpecificationMissingAnnotation( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithoutAnnotation.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The 'matcherSpecification' net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithoutAnnotation must be " +
                        "annotated with MatcherOf." ) ) );
        }
    }

    @Test
    public void matchingSpecificationAnnotationMismatch( ) throws Exception {
        // Setup

        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithAnnotationMismatch.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The MatcherOf annotation on net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithAnnotationMismatch must " +
                        "have a value of net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched." ) ) );
        }
    }

    @Test
    public void matchingInterfaceHasVoidMethod( ) throws Exception {
        // Setup
        // Exercise
        try
        {
            CompositeMatcherFactory.asSpecifiedBy(
                SpecificationWithVoidMethod.class,
                Matched.class );
            // Verify
            fail( );
        }
        catch ( final IllegalArgumentException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "The method public abstract void net.chwthewke.hamcrest.matchers." +
                        "CompositeMatcherFactoryTest$SpecificationWithVoidMethod.run() in specification " +
                        "net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$SpecificationWithVoidMethod " +
                        "has return type void." ) ) );
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

    @Test
    public void expectedPropertyThrowsException( ) throws Exception {
        // Setup
        final MatcherFactory<Matched> factory = asSpecifiedBy( ExpectedPropertyThrows.class );
        // Exercise
        try
        {
            factory.equivalentTo( new Matched( "" ) );
            // Verify
            fail( );
        }
        catch ( final RuntimeException e )
        {
            assertThat(
                e.getMessage( ),
                is( equalTo( "Exception while reading property getException on instance of " +
                        "net.chwthewke.hamcrest.matchers.CompositeMatcherFactoryTest$Matched." ) ) );
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

        public void run( ) {
        }

        public Object getException( ) {
            throw new RuntimeException( );
        }

        @SuppressWarnings( "unused" )
        private int getId( ) {
            return -1;
        }

        private final String value;
    }

    @MatcherOf( Matched.class )
    public static class SpecificationNotAnInterface {
    }

    @MatcherOf( Matched.class )
    static interface NonPublicInterface {
        @Equality
        String getValue( );
    }

    public static interface SpecificationWithoutAnnotation {
        @Equality
        String getValue( );
    }

    @MatcherOf( Object.class )
    public static interface SpecificationWithAnnotationMismatch {
        @Equality
        String getValue( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithNonPropertyMethod {
        @Equality
        String method( Object input );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithVoidMethod {
        @Equality
        void run( );
    }

    @MatcherOf( Matched.class )
    public static interface SpecificationWithMisnamedMethod {
        @Equality
        String value( );
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

    @MatcherOf( Matched.class )
    public static interface ExpectedPropertyThrows extends MatcherSpecification<Matched> {
        @Equality
        Object getException( );
    }

}