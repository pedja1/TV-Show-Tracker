/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api;

/**
 *
 * @author pedja
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/AppErrorHandler")
public class AppErrorHandler extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processError(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processError(request, response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // Analyze the servlet exception
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null)
        {
            servletName = "Unknown";
        }
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null)
        {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        JSONObject object = new JSONObject();
        object.put("success", -1);
        JSONObject error = new JSONObject();
        error.put("status_code", statusCode);
        error.put("requested_uri", requestUri);
        error.put("servlet_name", servletName);
        error.put("exception_name", throwable.getClass().getName());
        error.put("exception_message", throwable.getMessage());
        object.put("error", error);
        out.write(object.toString());
    }
}
