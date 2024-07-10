package model;

import model.enums.HttpStatus;
import util.FileMapper;
import util.HttpResponseConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    public String getProtocolVersion() {
        return protocolVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    private String protocolVersion;
    private HttpStatus httpStatus;
    private Map<String,String> headers = new HashMap<>();
    private byte[] body;

    private HttpResponse(String protocolVersion, HttpStatus httpStatus, Map<String,String> headers, byte[] body) {
        this.protocolVersion = protocolVersion;
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public static HttpResponse of(String protocolVersion, HttpStatus httpStatus, Map<String,String> headers, byte[] body) {
        return new HttpResponse(protocolVersion, httpStatus, headers, body);
    }


//    /**
//     * HttpRequest 객체로부터 HttpResponse 객체를 생성하는 팩터리메서드
//     * @param httpRequest 생성한 httpRequest 객체
//     * @return HttpResponse
//     */
//    public static HttpResponse from(HttpRequest httpRequest) {
//
//        // 확장자를 가지고 있으면 정적 파일
//
//        return null;
//    }
//
//    private HttpResponse createStaticHttpResponse() throws IOException {
////        String contentType = getContentTypeFromRequestPath();
//        List<String> headers = new ArrayList<>();
//
//        byte[] body = FileMapper.getByteConvertedFile(this.path);
//        headers.add("Content-Type: " + contentType + ";charset=utf-8\r\n");
//        headers.add("Content-Length: " + String.valueOf(body.length) + "\r\n");
//        headers.add("\r\n"); //Blank l
//        return new HttpResponse("HTTP/1.1", HttpStatus.OK, headers, body);
//    }







}
