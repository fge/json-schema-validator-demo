package com.github.fge.jsonschema.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jjschema.JJSchemaFromSource;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.ProcessingResult;
import com.github.fge.jsonschema.report.ListProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.util.ValueHolder;

import javax.ws.rs.Path;
import java.io.IOException;

import static com.github.fge.jsonschema.constants.ResponseFields.*;

@Path("/jjschema")
public final class JJSchemaProcessing
    extends Processing
{
    private static final JJSchemaFromSource PROCESSOR
        = JJSchemaFromSource.getInstance();

    @Override
    protected JsonNode buildResult(final String input)
        throws IOException, ProcessingException
    {
        final ValueHolder<String> holder = ValueHolder.hold("source", input);
        final ProcessingReport report = new ListProcessingReport();
        final ProcessingResult<ValueHolder<SchemaTree>> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, holder);

        final ProcessingReport processingReport = result.getReport();

        final ObjectNode ret = FACTORY.objectNode();
        final boolean success = processingReport.isSuccess();
        ret.put(VALID, success);

        final JsonNode content = success
            ? result.getResult().getValue().getBaseNode()
            : buildReport(result.getReport());

        ret.put(RESULTS, JacksonUtils.prettyPrint(content));
        return ret;
    }
}
