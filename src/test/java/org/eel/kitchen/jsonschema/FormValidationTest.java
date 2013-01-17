package org.eel.kitchen.jsonschema;

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
        when(request.getRequestDispatcher(Constants.RESULTS))
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
        when(request.getParameter("schema")).thenReturn("{}");
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Missing parameters");
        verify(request, never()).getRequestDispatcher(any(String.class));
    }

    @Test
    public void missingDataParameterReturns401()
        throws ServletException, IOException
    {
        when(request.getParameter("data")).thenReturn("{}");
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

        when(request.getParameter("schema")).thenReturn(schema);
        when(request.getParameter("data")).thenReturn(data);

        servlet.doPost(request, response);
        // FIXME: should get rid of pretty printing, it is not really useful
        verify(request).setAttribute(eq("data"), eq(data));

        verify(request).getRequestDispatcher(Constants.RESULTS);
        verify(requestDispatcher).forward(request, response);
    }
}
