package org.eel.kitchen.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eel.kitchen.jsonschema.util.jackson.JacksonUtils;

import java.io.IOException;
import java.io.StringWriter;

public final class Utils
{
    private static final ObjectWriter WRITER
        = JacksonUtils.getMapper().writerWithDefaultPrettyPrinter();

    private Utils()
    {
    }

    public static String prettyPrint(final JsonNode node)
        throws IOException
    {
        final StringWriter sw = new StringWriter();
        try {
            WRITER.writeValue(sw, node);
            sw.flush();
            return sw.toString();
        } finally {
            sw.close();
        }
    }
}
