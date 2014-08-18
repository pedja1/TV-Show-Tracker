/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tvst.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author pedja
 */
public class SyncController extends Controller
{

    public SyncController(MainServlet servlet, Map<String, String[]> requestParams)
    {
        super(servlet, requestParams);
    }

    @Override
    public void setResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action;
        int deleteCount = -1;
        int addCount = -1;
        List<String> items = null;
        PrintWriter writer = response.getWriter();
        if(!hasParam(RequestParam.action.toString()))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.missing_parameter.toString(), "Parameter 'action' is required"));
            return;
        }
        action = getParam(RequestParam.action.toString());
        if("sync".equals(action) && !hasParam(RequestParam.add.toString()) && !hasParam(RequestParam.delete.toString()))
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.missing_parameter.toString(), "You must specify either 'add[]' or 'delete[]' array, or both."));
            return;
        }
        DBUtility db = new DBUtility(servlet);
        if("sync".equals(action))
        {
            String[] addP = requestParams.get(RequestParam.add.toString());
            String[] deleteP = requestParams.get(RequestParam.delete.toString());

            if(addP != null)
            {
                db.insertItemsInTx(addP, servlet.getUserId());
                addCount = addP.length;
            }
            if(deleteP != null)
            {
                List<String> delete = Arrays.asList(deleteP); 
                db.deleteItemsInTx(deleteP, servlet.getUserId());
                deleteCount = deleteP.length;
            }
        }
        else if("get_all".equals(action))
        {
            items = db.getUsersItems(servlet.getUserId());
        }
        else
        {
            writer.print(Utility.generalErrorMessage(ResponseCode.invalid_parameter.toString(), "Invalid action '" + action + "'."));
            return;
        }
        
        JSONObject object = new JSONObject();
        object.put(JSONKey.status.toString(), 1);
        if(deleteCount != -1)object.put(JSONKey.deleted.toString(), deleteCount);
        if(addCount != -1)object.put(JSONKey.added.toString(), addCount);
        if(items != null)
        {
            JSONArray jsonArray = new JSONArray(items);
            object.put(JSONKey.items.toString(), jsonArray);
        }
        writer.print(object.toString(4));
        
    }
    
}
