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

package com.github.fge.jsonschema.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Closeables;
import org.eel.kitchen.jsonschema.util.JsonLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

/**
 * Return one sample schema/data pair
 *
 * <p>All tests are from the official <a
 * href="https://github.com/json-schema/JSON-Schema-Test-Suite">JSON Schema test
 * suite</a>.</p>
 */
public final class LoadSamplesServlet
    extends HttpServlet
{
    private static final Random RND = new Random();
    private static final List<JsonNode> SAMPLE_DATA;
    private static final int SAMPLE_DATA_SIZE;

    static {
        try {
            final JsonNode node = JsonLoader.fromResource("/samples.json");
            SAMPLE_DATA = ImmutableList.copyOf(node);
            SAMPLE_DATA_SIZE = SAMPLE_DATA.size();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req,
        final HttpServletResponse resp)
        throws ServletException, IOException
    {
        final int index = RND.nextInt(SAMPLE_DATA_SIZE);
        final JsonNode ret = SAMPLE_DATA.get(index);

        final OutputStream out = resp.getOutputStream();

        try {
            out.write(ret.toString().getBytes(Charset.forName("UTF-8")));
            out.flush();
        } finally {
            Closeables.closeQuietly(out);
        }
    }
}
