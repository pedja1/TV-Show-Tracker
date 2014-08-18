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
public enum RequestParam
{
    email, password, cmd, first_name, last_name, add("add[]"), delete("delete[]"),
    action;
    
    String mValue;
    RequestParam()
    {
        
    }
    
    RequestParam(String value)
    {
        mValue = value;
    }

    @Override
    public String toString()
    {
        return mValue == null ? super.toString() : mValue;
    }
}
