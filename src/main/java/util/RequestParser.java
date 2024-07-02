package util;

public class RequestParser {
    public static String parseUriFromRequestHeader(String requestHeader) {
        return requestHeader.split(" ")[1];

    }



}
