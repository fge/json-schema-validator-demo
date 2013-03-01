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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jjschema.JJSchemaFromSource;
import com.github.fge.jjschema.SourceHolder;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.processing.ProcessingResult;
import com.github.fge.jsonschema.processors.data.SchemaHolder;
import com.github.fge.jsonschema.report.ListProcessingReport;
import com.github.fge.jsonschema.report.LogLevel;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JacksonUtils;
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

@Path("/jjschema")
@Produces("application/json;charset=utf-8")
public final class JJSchema
{
    private static final Response OOPS = Response.status(500).build();

    private static final Logger log = LoggerFactory.getLogger(JJSchema.class);

    private static final JJSchemaFromSource PROCESSOR
        = JJSchemaFromSource.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public static Response checkSyntax(@FormParam("input") final String source)
    {
        try {
            final JsonNode ret = buildResult(source);
            return Response.ok().entity(ret.toString()).build();
        } catch (IOException e) {
            log.error("I/O error while building response", e);
            return OOPS;
        } catch (ProcessingException e) {
            log.error("Processing exception while building response", e);
            return OOPS;
        }
    }

    private static JsonNode buildResult(final String source)
        throws IOException, ProcessingException
    {
        final SourceHolder input = new SourceHolder(source);
        final ListProcessingReport report = new ListProcessingReport();
        final ProcessingResult<SchemaHolder> result
            = ProcessingResult.uncheckedResult(PROCESSOR, report, input);

        final ProcessingReport processingReport
            =  result.getReport();

        final ObjectNode ret = JsonNodeFactory.instance.objectNode();
        final boolean success = processingReport.isSuccess();
        ret.put(VALID, success);
        if (success) {
            final JsonNode node
                = result.getResult().getValue().getBaseNode();
            ret.put(RESULTS, JacksonUtils.prettyPrint(node));
        }
        else {
            final ListProcessingReport r
                = new ListProcessingReport(LogLevel.DEBUG, LogLevel.NONE);
            try {
                r.mergeWith(result.getReport());
            } catch (ProcessingException ignored) {
                // cannot happen
            }
            ret.put(RESULTS, JacksonUtils.prettyPrint(r.asJson()));
        }
        return ret;
    }
}
