package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.constants.ResponseFields;

import javax.ws.rs.Path;
import java.io.IOException;

@Path("/schema2pojo")
public final class Schema2PojoLoader
    extends SampleLoader
{
    private static final JsonNode SAMPLE_SCHEMA;

    static {
        try {
            SAMPLE_SCHEMA = JsonLoader.fromResource("/jsonschema2pojo.json");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    @Override
    protected JsonNode loadSample()
    {
        final ObjectNode ret = FACTORY.objectNode();
        ret.put(ResponseFields.INPUT, JacksonUtils.prettyPrint(SAMPLE_SCHEMA));
        return ret;
    }
}
