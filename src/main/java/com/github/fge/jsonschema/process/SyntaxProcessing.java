package com.github.fge.jsonschema.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;

import javax.ws.rs.Path;
import java.io.IOException;

import static com.github.fge.jsonschema.constants.ResponseFields.*;

@Path("/syntax")
public final class SyntaxProcessing
    extends Processing
{
    private static final SyntaxValidator VALIDATOR
        = new SyntaxValidator(ValidationConfiguration.byDefault());

    @Override
    protected JsonNode buildResult(final String input)
        throws IOException, ProcessingException
    {
        final ObjectNode ret = FACTORY.objectNode();

        final boolean invalidSchema = fillWithData(ret, INPUT, INVALID_INPUT,
            input);

        final JsonNode schemaNode = ret.remove(INPUT);

        if (invalidSchema)
            return ret;

        final ProcessingReport report = VALIDATOR.validateSchema(schemaNode);
        final boolean success = report.isSuccess();

        ret.put(VALID, success);
        ret.put(RESULTS, JacksonUtils.prettyPrint(buildReport(report)));
        return ret;
    }
}
