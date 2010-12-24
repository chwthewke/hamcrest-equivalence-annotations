package net.chwthewke.hamcrest;

import org.hamcrest.Matcher;

public interface MatcherFactory<T> {
    Matcher<T> equivalentTo( final T expected );
}
