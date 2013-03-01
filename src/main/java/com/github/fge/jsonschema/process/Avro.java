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

package com.github.fge.jsonschema.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.avro.Avro2JsonSchemaProcessor;
import com.github.fge.jsonschema.constants.ParseError;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.processing.ProcessingResult;
import com.github.fge.jsonschema.processing.Processor;
import com.github.fge.jsonschema.processing.ProcessorChain;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.processors.syntax.SyntaxProcessor;
import com.github.fge.jsonschema.report.ListProcessingReport;
import com.github.fge.jsonschema.report.LogLevel;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.tree.JsonTree;
import com.github.fge.jsonschema.tree.SimpleJsonTree;
import com.github.fge.jsonschema.util.JsonLoader;
import com.github.fge.jsonschema.util.ValueHolder;
import com.github.fge.util.SimpleValueHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.github.fge.jsonschema.constants.ResponseFields.*;

@Path("/avro")
@Produces("application/json;charset=utf-8")
public final class Avro
{
    private static final Response OOPS = Response.status(500).build();

    private static final Logger log = LoggerFactory.getLogger(Avro.class);

    private static final Processor<ValueHolder<JsonTree>, SchemaHolder>
        PROCESSOR;

    static {
        final Avro2JsonSchemaProcessor avro = new Avro2JsonSchemaProcessor();
        final SyntaxProcessor syntax
            = new SyntaxProcessor(DraftV4Library.get());
        PROCESSOR = ProcessorChain.startWith(avro).chainWith(syntax)
            .getProcessor();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response checkSyntax(@FormParam("input") final String schema)
    {
        try {
            final JsonNode ret = buildResult(schema);
            return Response.ok().entity(ret.toString()).build();
        } catch (IOException e) {
            log.error("I/O error while building response", e);
            return OOPS;
        }
    }


    /*
     * Build the response. When we arrive here, we are guaranteed that we have
     * the needed elements.
     */
    private static JsonNode buildResult(final String rawSchema)
        throws IOException
    {
        final ObjectNode ret = JsonNodeFactory.instance.objectNode();

        final boolean invalidSchema = fillWithData(ret, INPUT, INVALID_INPUT,
            rawSchema);

        final JsonNode schemaNode = ret.remove(INPUT);

        if (invalidSchema)
            return ret;

        final JsonTree tree = new SimpleJsonTree(schemaNode);
        final ValueHolder<JsonTree> input
            = new SimpleValueHolder<JsonTree>(tree);

        final ProcessingReport report = new ListProcessingReport();
        final ProcessingResult<SchemaHolder> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, input);
        final boolean success = result.isSuccess();
        final ListProcessingReport r = new ListProcessingReport(LogLevel.DEBUG,
            LogLevel.NONE);


        try {
            r.mergeWith(result.getReport());
        } catch (ProcessingException ignored) {
        }

        ret.put(VALID, success);
        ret.put(RESULTS, success
            ? result.getResult().getValue().getBaseNode()
            : r.asJson());
        return ret;
    }

    /*
     * We have to use that since Java is not smart enough to detect that
     * sometimes, a variable is initialized in all paths.
     *
     * This returns true if the data is invalid.
     */
    private static boolean fillWithData(final ObjectNode node,
        final String onSuccess, final String onFailure, final String raw)
        throws IOException
    {
        try {
            node.put(onSuccess, JsonLoader.fromString(raw));
            return false;
        } catch (JsonProcessingException e) {
            node.put(onFailure, ParseError.build(e, raw.contains("\r\n")));
            return true;
        }
    }
}
