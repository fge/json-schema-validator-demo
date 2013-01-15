package org.eel.kitchen.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eel.kitchen.jsonschema.main.JsonSchema;
import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.report.ValidationReport;
import org.eel.kitchen.jsonschema.util.JsonLoader;
import org.eel.kitchen.jsonschema.util.jackson.JacksonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

public final class FormValidation
    extends HttpServlet
{
    private static final JsonSchemaFactory FACTORY
        = JsonSchemaFactory.defaultFactory();
    private static final ObjectWriter WRITER = JacksonUtils.getMapper()
        .writerWithDefaultPrettyPrinter();


    @Override
    protected void doPost(final HttpServletRequest req,
        final HttpServletResponse resp)
        throws ServletException, IOException
    {
        // récupération des paramètres
        final String schemaParam = req.getParameter("schema");
        final String dataParam = req.getParameter("data");
        final StringWriter sw = new StringWriter();
        // traitement
        try {
            final JsonNode schemaNode = JsonLoader.fromString(schemaParam);
            final JsonNode dataNode = JsonLoader.fromString(dataParam);

            final JsonSchema schema = FACTORY.fromSchema(schemaNode);

            final ValidationReport report = schema.validate(dataNode);

            WRITER.writeValue(sw, report.asJsonObject());
            sw.flush();

            req.setAttribute("results", sw.toString());
        } catch (IOException e) {
            req.setAttribute("results", "ERROR: " + e.getMessage());
        } finally {
            sw.close();
        }
        // retour
        req.setAttribute("data", dataParam);
        req.getRequestDispatcher("/results.jsp").forward(req, resp);
    }
}
