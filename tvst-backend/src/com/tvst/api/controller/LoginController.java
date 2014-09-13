/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api.controller;

/**
 *
 * @author pedja
 */
import com.tvst.api.Constants;
import com.tvst.api.db.DBUtility;
import com.tvst.api.JSONKey;
import com.tvst.api.servlet.MainServlet;
import com.tvst.api.RequestParam;
import com.tvst.api.ResponseCode;
import com.tvst.api.utility.SessionGenerator;
import com.tvst.api.model.User;
import com.tvst.api.utility.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

public class LoginController extends Controller
{
    public LoginController(MainServlet servlet, Map<String, String[]> requestParams)
    {
        super(servlet, requestParams);
    }

    @Override
    public void setResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        String email = getParam(RequestParam.email.toString());
        String password = getParam(RequestParam.password.toString());
        if (Utility.isStringEmpty(email))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_not_provided.toString(), "Email is required!"));
            return;
        }
        if (Utility.isStringEmpty(password))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.password_not_provided.toString(), "Password is required!"));
            return;
        }

        DBUtility dbUtil = new DBUtility(servlet);
        User user = dbUtil.getUser(email, password);
        if (user != null)
        {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", user.getId());
            String sessionKey = SessionGenerator.getInstance().nextSessionId();
            Cookie cookie = new Cookie("auth_key", sessionKey);
            cookie.setMaxAge(Constants.COOKIE_AGE);
            response.addCookie(cookie);
            
            dbUtil.insertSession(sessionKey, user.getId());
            
            JSONObject jResponse = new JSONObject();
            jResponse.put(JSONKey.status.toString(), 0);
            //jResponse.put(JSONKey.auth_key.toString(), sessionKey);
            jResponse.put(JSONKey.user_info.toString(), user.toJSONObject());

            writer.print(jResponse.toString());
        }
        else
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_doesnt_exist.toString(), "Email address not found"));
        }
    }

}
