/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author pedja
 */
public final class SessionGenerator
{
    private static SessionGenerator generator;

    public static SessionGenerator getInstance()
    {
        if (generator == null)
        {
            generator = new SessionGenerator();
        }
        return generator;
    }

    private final SecureRandom random;

    public SessionGenerator()
    {
        this.random = new SecureRandom();
    }

    public String nextSessionId()
    {
        return new BigInteger(130, random).toString(32);
    }
}
