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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@WebServlet(name = "Logout", urlPatterns = { "/Logout" })
public class LogoutServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(LogoutServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals("JSESSIONID"))
                {
                    logger.info("JSESSIONID=" + cookie.getValue());
                    break;
                }
            }
        }
        //invalidate the session if exists
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            logger.info("User=" + session.getAttribute("User"));
            session.invalidate();
        }
        response.sendRedirect("login.html");
    }

}
