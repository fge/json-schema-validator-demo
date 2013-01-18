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

package org.eel.kitchen.jsonschema.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.common.net.MediaType;
import org.eel.kitchen.jsonschema.JsonSchemaFactories;
import org.eel.kitchen.jsonschema.Utils;
import org.eel.kitchen.jsonschema.constants.ServletInputs;
import org.eel.kitchen.jsonschema.main.JsonSchema;
import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.report.ValidationReport;
import org.eel.kitchen.jsonschema.util.JsonLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class FormValidation
    extends HttpServlet
{
    // Minimal accounting...
    private static final AtomicInteger COUNT = new AtomicInteger();

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

        // We don't allow duplicates
        while (enumeration.hasMoreElements())
            if (!params.add(enumeration.nextElement())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid parameters");
                return;
            }

        // We have required parameters
        if (!params.containsAll(ServletInputs.REQUIRED_PARAMS)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Missing parameters");
            return;
        }

        // We don't want extraneous parameters
        params.removeAll(ServletInputs.VALID_PARAMS);

        if (!params.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Invalid parameters");
            return;
        }

        final String rawSchema = req.getParameter(ServletInputs.SCHEMA);
        final String data = req.getParameter(ServletInputs.DATA);

        // Set correct content type
        resp.setContentType(MediaType.PLAIN_TEXT_UTF_8.toString());

        final boolean useV4
            = Boolean.parseBoolean(req.getParameter(ServletInputs.USE_V4));
        final boolean useId
            = Boolean.parseBoolean(req.getParameter(ServletInputs.USE_ID));

        final JsonNode ret = buildResult(rawSchema, data, useV4, useId);

        final PrintWriter writer = resp.getWriter();

        try {
            writer.write(Utils.prettyPrint(ret));
            writer.flush();
        } catch (IOException ignored) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            Closeables.closeQuietly(writer);
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

        final boolean invalidSchema = fillWithData(ret, "schema", rawSchema);
        ret.put("invalidSchema", invalidSchema);
        final boolean invalidData = fillWithData(ret, "data", rawData);
        ret.put("invalidData", invalidData);

        if (invalidSchema || invalidData) {
            ret.remove(Arrays.asList("schema", "data"));
            return ret;
        }

        final JsonNode schemaNode = ret.remove("schema");
        final JsonNode data = ret.remove("data");

        final JsonSchemaFactory factory
            = JsonSchemaFactories.withOptions(useV4, useId);

        final JsonSchema schema = factory.fromSchema(schemaNode);
        final ValidationReport report = schema.validate(data);

        ret.put("valid", report.isSuccess());
        ret.put("results", report.asJsonObject());
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
