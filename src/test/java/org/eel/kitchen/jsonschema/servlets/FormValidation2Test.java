package org.eel.kitchen.jsonschema.servlets;

import org.eel.kitchen.jsonschema.constants.ServletInputs;
import org.mockito.ArgumentMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

public final class FormValidation2Test
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
    private FormValidation2 servlet;

    @BeforeMethod
    public void init()
    {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servlet = new FormValidation2();
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
}
