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

import org.eel.kitchen.jsonschema.CustomMatchers;
import org.eel.kitchen.jsonschema.constants.ServletInputs;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public final class FormValidationTest
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FormValidation servlet;
    private PrintWriter writer;

    @BeforeMethod
    public void init()
        throws IOException
    {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        servlet = new FormValidation();
    }

    @Test
    public void missingBothParametersReturns401()
        throws ServletException, IOException
    {
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
        verify(request, never()).getRequestDispatcher(any(String.class));
    }

    @Test
    public void missingSchemaParameterReturns401()
        throws ServletException, IOException
    {
        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn("{}");
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
        verify(request, never()).getRequestDispatcher(any(String.class));
    }

    @Test
    public void missingDataParameterReturns401()
        throws ServletException, IOException
    {
        when(request.getParameter(ServletInputs.DATA)).thenReturn("{}");
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
        verify(request, never()).getRequestDispatcher(any(String.class));
    }

    @Test
    public void necessaryDataIsReturned()
        throws ServletException, IOException
    {
        final String schema = "{}";
        // FIXME: see below
        final String data = "{ }";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);

        verify(request).getParameter(ServletInputs.USE_V4);
        verify(request).getParameter(ServletInputs.USE_ID);

        verify(writer).write(eq(data));
    }

    @Test(dependsOnMethods = "necessaryDataIsReturned")
    public void invalidSchemaRaisesAnError()
        throws ServletException, IOException
    {
        final String schema = "foo";
        final String data = "{}";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);

        verify(writer).write(CustomMatchers.errorMessage());
    }

    @Test(dependsOnMethods = "necessaryDataIsReturned")
    public void invalidDataRaisesAnError()
        throws ServletException, IOException
    {
        final String schema = "{}";
        final String data = "foo";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);

        verify(writer).write(CustomMatchers.errorMessage());
    }
}
