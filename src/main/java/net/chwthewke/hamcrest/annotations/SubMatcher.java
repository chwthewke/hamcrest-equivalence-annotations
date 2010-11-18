package net.chwthewke.hamcrest.annotations;

import java.lang.reflect.Method;

import org.hamcrest.Matcher;

public class SubMatcher<T, U> {

    private Matcher<? super U> matcher;
    private Method extractor; // Function<T, U>
}
