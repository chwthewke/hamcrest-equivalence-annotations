package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import com.google.common.base.Function;

public class EqualitySubMatcherTemplateFactory<T, U> extends SubMatcherTemplateFactory<T, U> {

    protected EqualitySubMatcherTemplateFactory( final Method property, final String propertyName,
            final Class<U> propertyType ) {
        super( property, propertyName );
        this.propertyType = propertyType;
    }

    @Override
    public SubMatcherTemplate<T, U> getSubMatcherTemplate( ) {
        final Function<U, Matcher<? super U>> equalToMatcherFactory =
                new Function<U, Matcher<? super U>>( ) {
                    public Matcher<? super U> apply( final U expected ) {
                        return Matchers.equalTo( expected );
                    }
                };

        return SubMatcherTemplate.<T, U>create(
            propertyName,
            propertyFunction( property, propertyType ),
            equalToMatcherFactory );
    }

    private final Class<U> propertyType;

}
