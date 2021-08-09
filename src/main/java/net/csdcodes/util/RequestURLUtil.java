package net.csdcodes.util;

import javax.servlet.http.HttpServletRequest;

public class RequestURLUtil {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");

    }
}
