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

public final class FormValidation
    extends HttpServlet
{
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
