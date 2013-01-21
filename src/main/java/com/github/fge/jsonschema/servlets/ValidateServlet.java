/*
 * Copyright (c) 2013, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.fge.jsonschema.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.JsonSchemaFactories;
import com.github.fge.jsonschema.constants.ValidateRequest;
import com.github.fge.jsonschema.constants.ValidateResponse;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.common.net.MediaType;
import org.eel.kitchen.jsonschema.main.JsonSchema;
import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.report.ValidationReport;
import org.eel.kitchen.jsonschema.util.JsonLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Set;

/**
 * Servlet responsible of validating a schema/data pair
 *
 * <p>It returns a JSON Object as a result with the appropriate media type
 * (thanks Guava for providing {@link MediaType#JSON_UTF_8}!).</p>
 *
 * <p>This object has the following members:</p>
 *
 * <ul>
 *     <li>{@code invalidSchema}: boolean indicating whether the provided schema
 *     was valid JSON;</li>
 *     <li>{@code invalidData}: same, but for the data;</li>
 *     <li>{@code valid} (only if the schema and data are valid): whether the
 *     validation has succeeded;</li>
 *     <li>{@code results} (only if the schema and data are valid): the result
 *     of {@link ValidationReport#asJsonObject()}.</li>
 * </ul>
 */
public final class ValidateServlet
    extends HttpServlet
{
    @Override
    public void doPost(final HttpServletRequest req,
        final HttpServletResponse resp)
        throws ServletException, IOException
    {
        final Set<String> params = Sets.newHashSet();

        /*
         * First, check our parameters
         */
        /*
         * Why, in 2013, doesn't servlet-api provide an Iterator<String>?
         *
         * Well, at least, Jetty's implementation has a generified Enumeration.
         * Still, that sucks.
         */
        final Enumeration<String> enumeration = req.getParameterNames();

        // FIXME: no duplicates, it seems, but I cannot find the spec which
        // guarantees that
        while (enumeration.hasMoreElements())
            params.add(enumeration.nextElement());

        // We have required parameters
        if (!params.containsAll(ValidateRequest.REQUIRED_PARAMS)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Missing parameters");
            return;
        }

        // We don't want extraneous parameters
        params.removeAll(ValidateRequest.VALID_PARAMS);

        if (!params.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Invalid parameters");
            return;
        }

        final String rawSchema = req.getParameter(ValidateRequest.SCHEMA);
        final String data = req.getParameter(ValidateRequest.DATA);

        // Set correct content type
        resp.setContentType(MediaType.JSON_UTF_8.toString());

        final boolean useV4
            = Boolean.parseBoolean(req.getParameter(ValidateRequest.USE_V4));
        final boolean useId
            = Boolean.parseBoolean(req.getParameter(ValidateRequest.USE_ID));

        final JsonNode ret = buildResult(rawSchema, data, useV4, useId);

        final OutputStream out = resp.getOutputStream();

        try {
            out.write(ret.toString().getBytes(Charset.forName("UTF-8")));
            out.flush();
        } finally {
            Closeables.closeQuietly(out);
        }
    }

    /*
     * Build the response. When we arrive here, we are guaranteed that we have
     * the needed elements.
     */
    @VisibleForTesting
    static JsonNode buildResult(final String rawSchema,
        final String rawData, final boolean useV4, final boolean useId)
        throws IOException
    {
        final ObjectNode ret = JsonNodeFactory.instance.objectNode();

        final boolean invalidSchema = fillWithData(ret,
            ValidateResponse.SCHEMA, rawSchema);
        final boolean invalidData = fillWithData(ret,
            ValidateResponse.DATA, rawData);

        ret.put(ValidateResponse.INVALID_SCHEMA, invalidSchema);
        ret.put(ValidateResponse.INVALID_DATA, invalidData);

        final JsonNode schemaNode = ret.remove(ValidateResponse.SCHEMA);
        final JsonNode data = ret.remove(ValidateResponse.DATA);

        if (invalidSchema || invalidData)
            return ret;

        final JsonSchemaFactory factory
            = JsonSchemaFactories.withOptions(useV4, useId);

        final JsonSchema schema = factory.fromSchema(schemaNode);
        final ValidationReport report = schema.validate(data);

        ret.put(ValidateResponse.VALID, report.isSuccess());
        ret.put(ValidateResponse.RESULTS, report.asJsonObject());
        return ret;
    }

    /*
     * We have to use that since Java is not smart enough to detect that
     * sometimes, a variable is initialized in all paths.
     *
     * This returns true if the data is invalid.
     */
    private static boolean fillWithData(final ObjectNode node,
        final String fieldName, final String raw)
        throws IOException
    {
        try {
            node.put(fieldName, JsonLoader.fromString(raw));
            return false;
        } catch (JsonProcessingException ignored) {
            return true;
        }
    }
}
