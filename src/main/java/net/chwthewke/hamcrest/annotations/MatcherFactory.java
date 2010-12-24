package net.chwthewke.hamcrest.annotations;

import org.hamcrest.Matcher;

public interface MatcherFactory<T> {
    Matcher<T> equivalentTo( final T expected );
}
