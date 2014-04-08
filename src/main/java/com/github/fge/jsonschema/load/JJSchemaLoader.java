package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.constants.ResponseFields;
import com.google.common.io.ByteStreams;

import javax.ws.rs.Path;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Path("/jjschema")
public final class JJSchemaLoader
    extends SampleLoader
{
    private static final String SAMPLE_SOURCE;

    static {
        final InputStream resource
            = JJSchemaLoader.class.getResourceAsStream("/product.txt");
        if (resource == null)
            throw new ExceptionInInitializerError("sample source not found");

        try (
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final InputStream in = resource;
        ) {
            ByteStreams.copy(resource, out);
            out.flush();
            SAMPLE_SOURCE = new String(out.toByteArray(),
                Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    protected JsonNode loadSample()
    {
        final ObjectNode ret = FACTORY.objectNode();
        ret.put(ResponseFields.INPUT, FACTORY.textNode(SAMPLE_SOURCE));
        return ret;
    }
}
