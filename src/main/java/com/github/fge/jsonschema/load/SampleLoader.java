package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jsonschema.util.JacksonUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Produces("application/json;charset=utf-8")
public abstract class SampleLoader
{
    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    @GET
    public final Response getSample()
    {
        return Response.ok().entity(loadSample().toString()).build();
    }

    protected abstract JsonNode loadSample();
}
