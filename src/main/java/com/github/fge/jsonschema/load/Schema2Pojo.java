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

package com.github.fge.jsonschema.load;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.constants.ResponseFields;
import com.github.fge.jsonschema.util.JacksonUtils;
import com.github.fge.jsonschema.util.JsonLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/schema2pojo")
@Produces("application/json;charset=utf-8")
public final class Schema2Pojo
{
    private static final JsonNodeFactory FACTORY = JacksonUtils.nodeFactory();
    private static final JsonNode SAMPLE_SCHEMA;

    static {
        try {
            SAMPLE_SCHEMA = JsonLoader.fromResource("/jsonschema2pojo.json");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @GET
    public static Response getSampleSchema()
    {
        final ObjectNode node = FACTORY.objectNode();
        node.put(ResponseFields.INPUT, SAMPLE_SCHEMA);
        return Response.ok().entity(node.toString()).build();
    }
}
