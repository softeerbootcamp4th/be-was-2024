package common;

import web.HttpMethod;
import web.MIME;

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

    public static String getProperContentType(String extension) {
        return MIME.findByKey(extension).getType();
    }

    /**
     * path에 확장자가 없으면 REST 요청으로 간주
     */
    public static boolean isRESTRequest(String path) {
        return path.split("\\.").length==1;
    }
}
