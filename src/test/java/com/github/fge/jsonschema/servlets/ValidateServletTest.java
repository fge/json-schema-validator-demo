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
import com.github.fge.jsonschema.constants.ParseError;
import com.github.fge.jsonschema.constants.ValidateRequest;
import com.github.fge.jsonschema.constants.ValidateResponse;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public final class ValidateServletTest
{
    private final ValidateServlet servlet = new ValidateServlet();

    @Test(invocationCount = 50, threadPoolSize = 5)
    public void missingBothParametersReturns401()
        throws ServletException, IOException
    {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameterNames()).thenReturn(emptyEnumeration());
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
    }

    @Test(invocationCount = 50, threadPoolSize = 5)
    public void missingSchemaParameterReturns401()
        throws ServletException, IOException
    {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameterNames())
            .thenReturn(enumerationOf(ValidateRequest.DATA));
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
    }

    @Test(invocationCount = 50, threadPoolSize = 5)
    public void missingDataParameterReturns401()
        throws ServletException, IOException
    {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameterNames())
            .thenReturn(enumerationOf(ValidateRequest.SCHEMA));
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
    }

    @DataProvider
    public Iterator<Object[]> inputData()
    {
        return ImmutableSet.of(
            new Object[] { "", "", true, true },
            new Object[] { "{}", "", false, true },
            new Object[] { "", "{}", true, false },
            new Object[] { "{}", "{}", false, false }
        ).iterator();
    }

    @Test(
        dataProvider = "inputData",
        invocationCount = 50,
        threadPoolSize = 5
    )
    public void inputValidityIsCorrectlyDetected(final String rawSchema,
        final String rawData, final boolean invalidSchema,
        final boolean invalidData)
        throws IOException
    {
        final JsonNode node = ValidateServlet
            .buildResult(rawSchema, rawData, false, false);

        assertEquals(node.has(ValidateResponse.INVALID_SCHEMA), invalidSchema);
        assertEquals(node.has(ValidateResponse.INVALID_DATA), invalidData);

        JsonNode errnode;

        if (invalidSchema) {
            errnode = node.get(ValidateResponse.INVALID_SCHEMA);
            assertTrue(errnode.isObject());
            assertEquals(Sets.newHashSet(errnode.fieldNames()),
                ParseError.ALL_FIELDS);
        }

        if (invalidData) {
            errnode = node.get(ValidateResponse.INVALID_DATA);
            assertTrue(errnode.isObject());
            assertEquals(Sets.newHashSet(errnode.fieldNames()),
                ParseError.ALL_FIELDS);
        }
    }

    @DataProvider
    public Iterator<Object[]> sampleInputs()
    {
        return ImmutableSet.of(
            new Object[] { "\"hello\"", true },
            new Object[] { "0", false }
        ).iterator();
    }

    @Test(
        dataProvider = "sampleInputs",
        dependsOnMethods = "inputValidityIsCorrectlyDetected",
        invocationCount = 50,
        threadPoolSize = 5
    )
    public void validationResultsAreCorrectlyReported(final String rawData,
        final boolean valid)
        throws IOException
    {
        final String rawSchema = "{\"type\":\"string\"}";

        final JsonNode result = ValidateServlet
            .buildResult(rawSchema, rawData, false, false);

        final JsonNode results = result.get(ValidateResponse.RESULTS);
        final JsonNode node = result.get(ValidateResponse.VALID);

//        assertTrue(results.isObject());
        assertTrue(node.isBoolean());
        assertEquals(node.booleanValue(), valid);
        assertEquals(Sets.newHashSet(result.fieldNames()),
            ValidateResponse.OUTPUTS);
    }

    private static Enumeration<String> emptyEnumeration()
    {
        return new Enumeration<String>()
        {
            @Override
            public boolean hasMoreElements()
            {
                return false;
            }

            @Override
            public String nextElement()
            {
                throw new NoSuchElementException();
            }
        };
    }

    private static Enumeration<String> enumerationOf(final String... elements)
    {
        final List<String> list = Lists.newArrayList(elements);

        return new Enumeration<String>()
        {
            @Override
            public boolean hasMoreElements()
            {
                return !list.isEmpty();
            }

            @Override
            public String nextElement()
            {
                if (!hasMoreElements())
                    throw new NoSuchElementException();
                return list.remove(0);
            }
        };
    }
}
