package com.github.fge.jsonschema.dw.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public final class Software
{
    private final URI uri;
    private final String name;

    @JsonCreator
    public Software(@JsonProperty("uri") final URI uri,
        @JsonProperty("name") final String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public URI getUri()
    {
        return uri;
    }

    public String getName()
    {
        return name;
    }
}
