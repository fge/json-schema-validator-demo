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

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Path("/jjschema")
@Produces("text/plain;charset=utf-8")
public final class JJSchema
{
    private static final String SAMPLE_SOURCE;

    static {
        final InputStream in
            = JJSchema.class.getResourceAsStream("/product.txt");
        if (in == null)
            throw new ExceptionInInitializerError("sample source not found");

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteStreams.copy(in, out);
            out.flush();
            SAMPLE_SOURCE = new String(out.toByteArray(),
                Charset.forName("UTF-8"));
            Closeables.closeQuietly(out);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    @GET
    public static Response getSampleSource()
    {
        return Response.status(Response.Status.OK).entity(SAMPLE_SOURCE)
            .build();
    }
}
