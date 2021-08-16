package net.csdcodes.util;

import javax.servlet.http.HttpServletRequest;

public class RequestURLUtil {
    public static String getSiteURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        int first = url.indexOf("/");
        int second = url.indexOf("/",first + 1);
        int third = url.indexOf("/", second + 1);

        return url.substring(0,third);

    }
}
