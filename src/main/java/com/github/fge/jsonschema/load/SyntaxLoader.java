package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.constants.ResponseFields;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.JsonLoader;
import com.google.common.collect.ImmutableList;

import javax.ws.rs.Path;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Path("/syntax")
public final class SyntaxLoader
    extends SampleLoader
{
    private static final Random RND = new Random();
    private static final List<JsonNode> SAMPLE_DATA;
    private static final int SAMPLE_DATA_SIZE;

    static {
        try {
            final JsonNode node = JsonLoader.fromResource("/syntax.json");
            SAMPLE_DATA = ImmutableList.copyOf(node);
            SAMPLE_DATA_SIZE = SAMPLE_DATA.size();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    protected JsonNode loadSample()
    {
        final int index = RND.nextInt(SAMPLE_DATA_SIZE);
        final JsonNode sample = SAMPLE_DATA.get(index);
        final ObjectNode ret = FACTORY.objectNode();
        ret.put(ResponseFields.INPUT, JacksonUtils.prettyPrint(sample));
        return ret;
    }
}
