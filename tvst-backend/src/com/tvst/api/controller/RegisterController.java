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
import com.tvst.api.db.DBUtility;
import com.tvst.api.JSONKey;
import com.tvst.api.utility.MailUtility;
import com.tvst.api.servlet.MainServlet;
import com.tvst.api.RequestParam;
import com.tvst.api.ResponseCode;
import com.tvst.api.utility.SessionGenerator;
import com.tvst.api.model.User;
import com.tvst.api.utility.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.mail.MessagingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class RegisterController extends Controller
{

    public RegisterController(MainServlet servlet, Map<String, String[]> requestParams)
    {
        super(servlet, requestParams);
    }

    @Override
    public void setResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        PrintWriter writer = response.getWriter();
        String email = getParam(RequestParam.email.toString());
        String password = getParam(RequestParam.password.toString());
        String firstName = getParam(RequestParam.first_name.toString());
        String lastName = getParam(RequestParam.last_name.toString());
        String errorMsg = null;
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
        SessionGenerator generator = SessionGenerator.getInstance();
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastname(lastName);
        user.setPassword(password);
        user.setRegistration_key(generator.nextSessionId());
        boolean result = dbUtil.insertUser(user);
        if (result)
        {
            try
            {
                MailUtility.sendVerificationEmail(user);
            }
            catch (MessagingException ex)
            {
                ex.printStackTrace();
            }

            JSONObject jReponse = new JSONObject();
            jReponse.put(JSONKey.status.toString(), 0);
            jReponse.put(JSONKey.message.toString(), "Registration successful. Check your email for instructions on how to verify your account.");

            writer.print(jReponse.toString());
        }
        else
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.email_exists.toString(), "Email address already exists"));
        }
    }

}
