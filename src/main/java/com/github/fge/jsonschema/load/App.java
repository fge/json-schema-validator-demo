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

        builder.add(IndexLoader.class);
        builder.add(JJSchemaLoader.class);
        builder.add(Schema2PojoLoader.class);
        builder.add(SyntaxLoader.class);
        builder.add(AvroLoader.class);

        return builder.build();
    }
}
