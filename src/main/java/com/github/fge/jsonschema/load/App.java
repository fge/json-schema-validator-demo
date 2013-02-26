package com.github.fge.jsonschema.load;

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

        builder.add(TestSuite.class);
        builder.add(SampleSource.class);
        builder.add(SampleSchema.class);

        return builder.build();
    }
}
