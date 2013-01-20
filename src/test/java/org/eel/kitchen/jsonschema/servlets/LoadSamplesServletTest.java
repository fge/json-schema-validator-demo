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

package org.eel.kitchen.jsonschema.servlets;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import org.eel.kitchen.jsonschema.constants.ValidateRequest;
import org.eel.kitchen.jsonschema.util.JsonLoader;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class LoadSamplesServletTest
{
    private final LoadSamplesServlet servlet = new LoadSamplesServlet();

    @Test(invocationCount = 50, threadPoolSize = 5)
    public void inputDataHasTheCorrectShape()
        throws ServletException, IOException
    {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ByteArrayOutputStream collector = new ByteArrayOutputStream();
        final ServletOutputStream out = new ServletOutputStream()
        {
            @Override
            public void write(int b)
                throws IOException
            {
                collector.write(b);
            }
        };
        when(response.getOutputStream()).thenReturn(out);

        servlet.doGet(request, response);

        final String json = collector.toString("UTF-8");
        final JsonNode node = JsonLoader.fromString(json);

        assertEquals(Sets.newHashSet(node.fieldNames()),
            ValidateRequest.VALID_PARAMS);
    }
}
