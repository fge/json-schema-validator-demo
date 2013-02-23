package com.github.fge.jsonschema.app;

import com.github.fge.jsonschema.servlets2.LoadSamplesServlet2;
import com.github.fge.jsonschema.servlets2.SyntaxValidateServlet2;
import com.google.common.collect.ImmutableSet;

import javax.ws.rs.core.Application;
import java.util.Set;

public final class App
    extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();

        builder.add(LoadSamplesServlet2.class);
        builder.add(SyntaxValidateServlet2.class);

        return builder.build();
    }
}
