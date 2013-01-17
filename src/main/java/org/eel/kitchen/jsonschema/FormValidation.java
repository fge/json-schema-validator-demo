package org.eel.kitchen.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
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
        final String rawSchema = req.getParameter("schema");
        String data = req.getParameter("data");

        if (rawSchema == null || data == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                "Missing parameters");
            return;
        }
        req.setAttribute("origSchema", rawSchema);

        try {
            final JsonNode schemaNode = JsonLoader.fromString(rawSchema);
            final JsonNode dataNode = JsonLoader.fromString(data);
            data = Utils.prettyPrint(dataNode);

            final JsonSchema schema = FACTORY.fromSchema(schemaNode);

            final ValidationReport report = schema.validate(dataNode);

            req.setAttribute("results",
                Utils.prettyPrint(report.asJsonObject()));
        } catch (IOException e) {
            req.setAttribute("results", "ERROR: " + e.getMessage());
        }
        // retour
        req.setAttribute("data", data);
        req.getRequestDispatcher("/results.jsp").forward(req, resp);
    }
}
