/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api;

import java.io.Serializable;
import org.json.JSONObject;

/**
 *
 * @author pedja
 */
public class User implements Serializable
{

    private static final long serialVersionUID = 6297385302078200511L;
    private int id;
    private String firstName, lastname, email, password, registration_key;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRegistration_key()
    {
        return registration_key;
    }

    public void setRegistration_key(String registration_key)
    {
        this.registration_key = registration_key;
    }
    
    
    
    public JSONObject toJSONObject()
    {
        JSONObject object = new JSONObject();
        object.put(JSONKey.id.toString(), id);
        object.put(JSONKey.email.toString(), email);
        object.put(JSONKey.first_name.toString(), firstName);
        object.put(JSONKey.last_name.toString(), lastname);
        return object;
    }

}
