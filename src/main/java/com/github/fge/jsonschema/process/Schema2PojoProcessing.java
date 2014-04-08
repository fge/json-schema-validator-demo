package com.github.fge.jsonschema.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.processing.ProcessingResult;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.CanonicalSchemaTree;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.core.util.ValueHolder;
import com.github.fge.jsonschema2pojo.JsonSchema2SourceCode;

import javax.ws.rs.Path;
import java.io.IOException;

import static com.github.fge.jsonschema.constants.ResponseFields.*;

@Path("/schema2pojo")
public final class Schema2PojoProcessing
    extends Processing
{
    private static final JsonSchema2SourceCode PROCESSOR
        = new JsonSchema2SourceCode();

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

        final SchemaTree tree = new CanonicalSchemaTree(schemaNode);
        final ValueHolder<SchemaTree> holder = ValueHolder.hold("schema", tree);

        final ProcessingReport report = new ListProcessingReport();
        final ProcessingResult<ValueHolder<String>> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, holder);

        final boolean  success = result.isSuccess();
        ret.put(VALID, success);

        final String content = success
            ? result.getResult().getValue()
            : JacksonUtils.prettyPrint(buildReport(result.getReport()));

        ret.put(RESULTS, content);
        return ret;
    }
}
