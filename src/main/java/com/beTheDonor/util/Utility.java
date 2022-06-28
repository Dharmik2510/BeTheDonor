package com.beTheDonor.util;

import javax.servlet.http.HttpServletRequest;

public class Utility
{
    public static String getUrl(HttpServletRequest request)
    {
        String url  = request.getRequestURL().toString();
        return url.replace(request.getServletPath(),"");

    }
}
