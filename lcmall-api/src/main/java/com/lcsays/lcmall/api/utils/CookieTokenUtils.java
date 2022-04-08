package com.lcsays.lcmall.api.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: lichuang
 * @Date: 21-8-30 10:39
 */

public class CookieTokenUtils {
    private static final String TOKEN_KEY = "token";
    private static final String COOKIE_PATH = "/";

    public static String getTokenValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : request.getCookies()) {
                if (TOKEN_KEY.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String setTokenValue(HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(TOKEN_KEY, token);
        cookie.setPath(COOKIE_PATH);
        response.addCookie(cookie);
        return token;
    }

    public static String setTokenValue(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(TOKEN_KEY, token);
        cookie.setPath(COOKIE_PATH);
        response.addCookie(cookie);
        return token;
    }

    public static void deleteTokenValue(HttpServletResponse response) {
        String token = "";
        Cookie cookie = new Cookie(TOKEN_KEY, token);
        cookie.setPath(COOKIE_PATH);
        response.addCookie(cookie);
    }
}
