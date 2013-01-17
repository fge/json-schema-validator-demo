package org.eel.kitchen.jsonschema;

import org.mockito.ArgumentMatcher;

import static org.mockito.Matchers.*;

public final class CustomMatchers
{
    private static final ArgumentMatcher<String> IS_ERROR
        = new ArgumentMatcher<String>()
    {
        @Override
        public boolean matches(final Object argument)
        {
            return ((String) argument).startsWith("ERROR: ");
        }
    };

    private CustomMatchers()
    {
    }

    public static String errorMessage()
    {
        return argThat(IS_ERROR);
    }
}
