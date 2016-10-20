package com.thinkive.market;

import com.thinkive.base.config.Configuration;

public class Library
{
    static private final String TIMEZONE = Configuration.getString("general.timezone");
    
    static private final String ENCODING = "UTF-8";
    
    public static String getEncoding()
    {
        return ENCODING;
    }
    
    public static String getTimeZone()
    {
        return TIMEZONE;
    }
}
