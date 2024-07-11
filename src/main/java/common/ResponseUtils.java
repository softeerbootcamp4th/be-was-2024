package common;

import web.HttpResponse;
import web.MIME;
import web.ResponseCode;
import web.ViewPath;

import java.util.Map;

public class ResponseUtils {

    public static HttpResponse redirectToView(ViewPath viewPath) {
        return new HttpResponse.HttpResponseBuilder()
                .code(ResponseCode.FOUND)
                .location(viewPath.getFilePath())
                .build();
    }

    public static HttpResponse redirectToViewWithCookie(Map<String, String> cookie) {
        return new HttpResponse.HttpResponseBuilder()
                .code(ResponseCode.FOUND)
                .cookie(cookie)
                .build();
    }

    public static HttpResponse responseSuccess() {
        return new HttpResponse.HttpResponseBuilder()
                .code(ResponseCode.OK)
                .build();
    }

    public static HttpResponse responseSuccessWithJson(int length, byte[] jsonBody) {
        return new HttpResponse.HttpResponseBuilder()
                .code(ResponseCode.OK)
                .contentType(MIME.JSON.getType())
                .contentLength(length)
                .body(jsonBody)
                .build();
    }

    public static HttpResponse responseBadRequest() {
        return new HttpResponse.HttpResponseBuilder()
                .code(ResponseCode.BAD_REQUEST)
                .build();
    }

    public static HttpResponse createResponse(ResponseCode code, String contentType) {
        return new HttpResponse.HttpResponseBuilder().code(code).contentType(contentType).build();
    }

}

