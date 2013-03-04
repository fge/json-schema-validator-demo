package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.constants.ResponseFields;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

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
        final InputStream in
            = JJSchemaLoader.class.getResourceAsStream("/product.txt");
        if (in == null)
            throw new ExceptionInInitializerError("sample source not found");

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteStreams.copy(in, out);
            out.flush();
            SAMPLE_SOURCE = new String(out.toByteArray(),
                Charset.forName("UTF-8"));
            Closeables.closeQuietly(out);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            Closeables.closeQuietly(in);
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
