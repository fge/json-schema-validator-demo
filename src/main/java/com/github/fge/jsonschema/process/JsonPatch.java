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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonpatch.JsonPatchInput;
import com.github.fge.jsonpatch.JsonPatchProcessor;
import com.github.fge.jsonschema.constants.ParseError;
import com.github.fge.jsonschema.core.processing.ProcessingResult;
import com.github.fge.jsonschema.core.report.ListProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.util.ValueHolder;
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
import java.io.StringReader;

import static com.github.fge.jsonschema.constants.ResponseFields.*;


@Path("/jsonpatch")
@Produces("application/json;charset=utf-8")
public final class JsonPatch
{
    private static final Logger log = LoggerFactory.getLogger(JsonPatch.class);
    private static final Response OOPS = Response.status(500).build();
    private static final JsonPatchProcessor PROCESSOR
        = new JsonPatchProcessor();
    private static final ObjectMapper MAPPER = JacksonUtils.newMapper();
    private static final JsonNodeReader NODE_READER
        = new JsonNodeReader();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response validate(@FormParam("input") final String patch,
        @FormParam("input2") final String data
    )
    {
        try {
            final JsonNode ret = buildResult(patch, data);
            return Response.ok().entity(ret.toString()).build();
        } catch (IOException e) {
            log.error("I/O error while validating data", e);
            return OOPS;
        }
    }


    /*
     * Build the response. When we arrive here, we are guaranteed that we have
     * the needed elements.
     */
    private static JsonNode buildResult(final String rawPatch,
        final String rawData)
        throws IOException
    {
        final ObjectNode ret = JsonNodeFactory.instance.objectNode();

        final boolean invalidSchema = fillWithData(ret, INPUT, INVALID_INPUT,
            rawPatch);
        final boolean invalidData = fillWithData(ret, INPUT2, INVALID_INPUT2,
            rawData);

        final JsonNode patchNode = ret.remove(INPUT);
        final JsonNode data = ret.remove(INPUT2);

        if (invalidSchema || invalidData)
            return ret;

        final JsonPatchInput input = new JsonPatchInput(patchNode, data);

        final ProcessingReport report = new ListProcessingReport();
        final ProcessingResult<ValueHolder<JsonNode>> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, input);

        final boolean success = result.isSuccess();
        ret.put(VALID, success);
        final JsonNode node = result.isSuccess() ? result.getResult()
            .getValue() : buildReport(result.getReport());
        ret.put(RESULTS, JacksonUtils.prettyPrint(node));
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
            node.put(onSuccess, NODE_READER.fromReader(new StringReader(raw)));
            return false;
        } catch (JsonProcessingException e) {
            node.put(onFailure, ParseError.build(e, raw.contains("\r\n")));
            return true;
        }
    }

    private static JsonNode buildReport(final ProcessingReport report)
    {
        final ArrayNode ret = JacksonUtils.nodeFactory().arrayNode();
        for (final ProcessingMessage message: report)
            ret.add(message.asJson());
        return ret;
    }
}
