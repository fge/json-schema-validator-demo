package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.google.common.collect.ImmutableList;

import javax.ws.rs.Path;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Path("/jsonpatch")
public final class JsonPatchLoader
    extends SampleLoader
{
    private static final Random RND = new Random();
    private static final List<JsonNode> SAMPLE_DATA;
    private static final int SAMPLE_DATA_SIZE;

    static {
        try {
            final JsonNode node = JsonLoader.fromResource("/jsonpatch.json");
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
        final Map<String, JsonNode> map = JacksonUtils.asMap(sample);
        for (final Map.Entry<String, JsonNode> entry: map.entrySet())
            ret.put(entry.getKey(), JacksonUtils.prettyPrint(entry.getValue()));

        return ret;
    }
}
