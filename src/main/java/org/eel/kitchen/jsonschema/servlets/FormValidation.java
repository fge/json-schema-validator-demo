package org.eel.kitchen.jsonschema.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import org.eel.kitchen.jsonschema.Utils;
import org.eel.kitchen.jsonschema.constants.Pages;
import org.eel.kitchen.jsonschema.constants.ServletInputs;
import org.eel.kitchen.jsonschema.constants.ServletOutputs;
import org.eel.kitchen.jsonschema.main.JsonSchema;
import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.report.ValidationReport;
import org.eel.kitchen.jsonschema.util.JsonLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class FormValidation
    extends HttpServlet
{
    private static final JsonSchemaFactory FACTORY
        = JsonSchemaFactory.defaultFactory();

    @Override
    public void doPost(final HttpServletRequest req,
        final HttpServletResponse resp)
        throws ServletException, IOException
    {
        final String rawSchema = req.getParameter(ServletInputs.SCHEMA);
        String data = req.getParameter(ServletInputs.DATA);

        if (rawSchema == null || data == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Missing parameters");
            return;
        }

        try {
            final JsonNode schemaNode = JsonLoader.fromString(rawSchema);
            final JsonNode dataNode = JsonLoader.fromString(data);
            data = Utils.prettyPrint(dataNode);

            final JsonSchema schema = FACTORY.fromSchema(schemaNode);

            final ValidationReport report = schema.validate(dataNode);

            req.setAttribute(ServletOutputs.RESULTS,
                Utils.prettyPrint(report.asJsonObject()));
        } catch (IOException e) {
            req.setAttribute(ServletOutputs.RESULTS,
                "ERROR: " + e.getMessage());
        }

        req.setAttribute(ServletOutputs.DATA, data);
        req.getRequestDispatcher(Pages.RESULTS).forward(req, resp);
    }
}
