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

import com.fasterxml.jackson.databind.JsonNode;
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
        final String rawSchema = req.getParameter(ServletInputs.SCHEMA);
        final String data = req.getParameter(ServletInputs.DATA);

        if (rawSchema == null || data == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Missing parameters");
            return;
        }

        // Set correct content type
        resp.setContentType(MediaType.PLAIN_TEXT_UTF_8.toString());

        final boolean useV4
            = Boolean.parseBoolean(req.getParameter(ServletInputs.USE_V4));
        final boolean useId
            = Boolean.parseBoolean(req.getParameter(ServletInputs.USE_ID));

        final JsonSchemaFactory factory
            = JsonSchemaFactories.withOptions(useV4, useId);

        System.out.println("Invocation " + COUNT.incrementAndGet()
            + ": useV4 " + useV4 + ", useId " + useId);

        final PrintWriter writer = resp.getWriter();

        try {
            final JsonNode schemaNode = JsonLoader.fromString(rawSchema);
            final JsonNode dataNode = JsonLoader.fromString(data);

            final JsonSchema schema = factory.fromSchema(schemaNode);

            final ValidationReport report = schema.validate(dataNode);

            writer.write(Utils.prettyPrint(report.asJsonObject()));
        } catch (IOException e) {
            writer.write("ERROR: " + e.getMessage());
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
