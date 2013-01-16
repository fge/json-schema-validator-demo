package org.eel.kitchen.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.eel.kitchen.jsonschema.util.jackson.JacksonUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;

public final class Utils
{
    private static final String RANDOM_UUID = UUID.randomUUID().toString();

    private static final ObjectWriter WRITER
        = JacksonUtils.getMapper().writerWithDefaultPrettyPrinter();

    private static final Map<Character, String> ENTITIES
        = ImmutableMap.<Character, String>builder()
            .put('"', "&quot;")
            .put('<', "&lt;")
            .put('&', "&amp;")
            .build();

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

    public static String getHash(final String first, final String... other)
    {
        final HashFunction f = Hashing.sha1();

        final Hasher hasher = f.newHasher();

        hasher.putString(first);

        for (final String s: other)
            hasher.putString(s);

        final HashCode hash = hasher.hash();

        return hash.toString();
    }

    /*
     * FIXME: this needs to go, I need to do HTML5
     */
    public static String inputValueEscape(final String input)
    {
        final int len = input.length();
        final StringBuilder sb = new StringBuilder(len);

        char c;
        String entity;

        for (int i = 0; i < len; i++) {
            c = input.charAt(i);
            entity = ENTITIES.get(c);
            if (entity == null)
                sb.append(c);
            else
                sb.append(entity);
        }

        return sb.toString();
    }
}
