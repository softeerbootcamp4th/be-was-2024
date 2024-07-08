package common;

import web.ContentType;
import web.HttpMethod;

/**
 * Web 요청을 처리할 때 공통으로 사용할 로직을 정리한 유틸리티 클래스
 */
public class WebUtils {

    public static boolean isMethodHeader(String method) {
        try {
            HttpMethod.valueOf(method.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isGetRequest(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase()).equals(HttpMethod.GET);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isPostRequest(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase()).equals(HttpMethod.POST);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String getProperContentType(String extension) {
        return ContentType.findByKey(extension).getType();
    }

    /**
     * path에 확장자가 없으면 REST 요청으로 간주
     */
    public static boolean isRESTRequest(String path) {
        return path.split("\\.").length==1;
    }
}
