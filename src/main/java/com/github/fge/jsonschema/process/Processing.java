package com.github.fge.jsonschema.process;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.constants.ParseError;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Produces("application/json;charset=utf-8")
public abstract class Processing
{
    private static final Response OOPS = Response.status(500).build();

    protected static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();

    private final Logger logger;

    protected Processing()
    {
        logger = LoggerFactory.getLogger(getClass());
    }


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public final Response doProcess(@FormParam("input") final String input)
    {
        try {
            final JsonNode ret = buildResult(input);
            final String out = JacksonUtils.prettyPrint(ret);
            return Response.ok().entity(out).build();
        } catch (IOException e) {
            logger.error("I/O error while building response", e);
            return OOPS;
        } catch (ProcessingException e) {
            logger.error("Processing exception while building response", e);
            return OOPS;
        }
    }

    protected abstract JsonNode buildResult(final String input)
        throws IOException, ProcessingException;

    protected static boolean fillWithData(final ObjectNode node,
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

    protected static JsonNode buildReport(final ProcessingReport report)
    {
        final ArrayNode ret = FACTORY.arrayNode();
        for (final ProcessingMessage message: report)
            ret.add(message.asJson());
        return ret;
    }
}
