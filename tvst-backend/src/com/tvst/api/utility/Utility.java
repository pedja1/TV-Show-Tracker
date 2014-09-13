/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tvst.api.utility;

import com.tvst.api.JSONKey;

import org.json.JSONObject;

/**
 *
 * @author pedja
 */
public class Utility
{
    public static String generalErrorMessage(String errorCode, String errorMessage)
    {
        JSONObject object = new JSONObject();
        object.put(JSONKey.status.toString(), -1);
        object.put(JSONKey.error_code.toString(), errorCode);
        object.put(JSONKey.error_message.toString(), errorMessage);
        return object.toString();
    }
    
    public static boolean isStringEmpty(String value)
    {
        return value == null || value.equals("");
    }
}
