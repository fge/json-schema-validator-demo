package org.eel.kitchen.jsonschema.servlets;

import org.eel.kitchen.jsonschema.constants.Pages;
import org.eel.kitchen.jsonschema.constants.ServletInputs;
import org.eel.kitchen.jsonschema.constants.ServletOutputs;
import org.eel.kitchen.jsonschema.servlets.FormValidation;
import org.mockito.ArgumentMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

public final class FormValidationTest
{
    private static final ArgumentMatcher<String> IS_ERROR
        = new ArgumentMatcher<String>()
    {
        @Override
        public boolean matches(final Object argument)
        {
            return ((String) argument).startsWith("ERROR: ");
        }
    };

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;
    private FormValidation servlet;

    @BeforeMethod
    public void init()
    {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(Pages.RESULTS))
            .thenReturn(requestDispatcher);
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
    public void necessaryDataIsDispatchedToNextPage()
        throws ServletException, IOException
    {
        final String schema = "{}";
        // FIXME: see below
        final String data = "{ }";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);
        // FIXME: should get rid of pretty printing, it is not really useful
        verify(request).setAttribute(same(ServletOutputs.DATA), eq(data));

        verify(request).getRequestDispatcher(Pages.RESULTS);
        verify(requestDispatcher).forward(request, response);
    }

    @Test(dependsOnMethods = "necessaryDataIsDispatchedToNextPage")
    public void invalidSchemaRaisesAnError()
        throws ServletException, IOException
    {
        final String schema = "foo";
        final String data = "{}";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);

        verify(request).setAttribute(same(ServletOutputs.RESULTS),
            argThat(IS_ERROR));
    }

    @Test(dependsOnMethods = "necessaryDataIsDispatchedToNextPage")
    public void invalidDataRaisesAnError()
        throws ServletException, IOException
    {
        final String schema = "{}";
        final String data = "foo";

        when(request.getParameter(ServletInputs.SCHEMA)).thenReturn(schema);
        when(request.getParameter(ServletInputs.DATA)).thenReturn(data);

        servlet.doPost(request, response);

        verify(request).setAttribute(same(ServletOutputs.RESULTS),
            argThat(IS_ERROR));
    }
}
