package common;

import file.ViewFile;
import web.HttpMethod;
import web.HttpRequest;
import webserver.WebAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

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
        return method.equals("GET");
    }

    public static boolean isPostRequest(String method) {
        return method.equals("POST");
    }

    public static String getProperContentType(String extension) {
        return switch (extension) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "ico" -> "image/x-icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpeg";
            case "svg" -> "image/svg+xml";
            default -> "*/*";
        };
    }

    /**
     * path에 확장자가 없으면 REST 요청으로 간주
     */
    public static boolean isRESTRequest(String path) {
        return path.split("\\.").length==1;
    }
}
