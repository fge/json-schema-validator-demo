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
import com.github.fge.jsonschema.constants.ParseError;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.AsJson;
import com.github.fge.jsonschema.util.JsonLoader;
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

@Path("/index")
@Produces("application/json;charset=utf-8")
public final class Index
{
    private static final String INVALID_SCHEMA = "invalidSchema";
    private static final String INVALID_DATA = "invalidData";
    private static final String RESULTS = "results";
    private static final String VALID = "valid";

    private static final String INPUT = "input";
    private static final String INPUT2 = "input2";
    private static final Logger log = LoggerFactory.getLogger(Index.class);
    private static final Response OOPS = Response.status(500).build();
    private static final JsonValidator VALIDATOR
        = JsonSchemaFactory.byDefault().getValidator();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response validate(
        @FormParam("input") final String schema,
        @FormParam("input2") final String data
    )
    {
        try {
            final JsonNode ret = buildResult(schema, data);
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
    private static JsonNode buildResult(final String rawSchema,
        final String rawData)
        throws IOException
    {
        final ObjectNode ret = JsonNodeFactory.instance.objectNode();

        final boolean invalidSchema = fillWithData(ret, INPUT, INVALID_SCHEMA,
            rawSchema);
        final boolean invalidData = fillWithData(ret, INPUT2, INVALID_DATA,
            rawData);

        final JsonNode schemaNode = ret.remove(INPUT);
        final JsonNode data = ret.remove(INPUT2);

        if (invalidSchema || invalidData)
            return ret;

        final ProcessingReport report
            = VALIDATOR.validateUnchecked(schemaNode, data);

        final boolean success = report.isSuccess();
        ret.put(VALID, success);
        ret.put(RESULTS, ((AsJson) report).asJson());
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
