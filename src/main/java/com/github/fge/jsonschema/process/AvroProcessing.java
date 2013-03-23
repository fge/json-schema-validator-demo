package com.github.fge.jsonschema.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.avro.Avro2JsonSchemaProcessor;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.processing.ProcessingResult;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processing.ProcessorChain;
import com.github.fge.jsonschema.report.ListProcessingReport;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.syntax.SyntaxProcessor;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.tree.SchemaTree;
import com.github.fge.jsonschema.tree.SimpleJsonTree;
import com.github.fge.jsonschema.util.ValueHolder;

import javax.ws.rs.Path;
import java.io.IOException;

import static com.github.fge.jsonschema.constants.ResponseFields.*;

@Path("/avro")
public final class AvroProcessing
    extends Processing
{
    private static final Processor<ValueHolder<JsonTree>, ValueHolder<SchemaTree>>
        PROCESSOR;

    static {
        final Avro2JsonSchemaProcessor avro = new Avro2JsonSchemaProcessor();
        final SyntaxProcessor syntax
            = new SyntaxProcessor(DraftV4Library.get().getSyntaxCheckers());
        PROCESSOR = ProcessorChain.startWith(avro).chainWith(syntax)
            .getProcessor();
    }

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

        final JsonTree tree = new SimpleJsonTree(schemaNode);
        final ValueHolder<JsonTree> holder = ValueHolder.hold(tree);

        final ProcessingReport report = new ListProcessingReport();
        final ProcessingResult<ValueHolder<SchemaTree>> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, holder);
        final boolean success = result.isSuccess();

        final JsonNode content = success
            ? result.getResult().getValue().getBaseNode()
            : buildReport(result.getReport());

        ret.put(VALID, success);
        ret.put(RESULTS, JacksonUtils.prettyPrint(content));
        return ret;
    }
}
