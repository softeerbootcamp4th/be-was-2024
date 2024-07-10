package http.utils;

import http.cookie.MyCookies;

import java.util.List;

public class HttpParseUtil {
    public static String[] parseRequestLine(String requestLine) {
        // method url version 정보가 포함되어야 한다. 각각은 공백을 기준으로 나눔.
        String[] line_info = requestLine.split(" ");
        // 3개의 정보가 아닌 경우 => 제거
        if(line_info.length != 3) throw new RuntimeException("invalid http request line");

        return line_info;
    }

    public static MyCookies parseCookies(String cookieKvString) {
        MyCookies cookies = new MyCookies();
        // null이면 빈 객체 반환
        if(cookieKvString == null) return cookies;

        String[] eachCookies = cookieKvString.split(";");
        for(String eachCookie : eachCookies) {
            String[] cookieInfo = eachCookie.split("=", 2);
            if(cookieInfo.length != 2) continue; // 잘못된 쿠키면 그냥 무시
            String key = cookieInfo[0].trim();
            String value = cookieInfo[1].trim();

            cookies.put(key, value);
        }
        return cookies;
    }
}
