package model;

import db.Database;
import model.HttpResponse;
import model.User;
import model.enums.HttpMethod;
import model.enums.HttpStatus;
import util.FileMapper;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Map;

public class HttpRequest {


    private final HttpMethod httpMethod;
    private final String path;
    private final Map<String, String> queryParams;
    private final String protocolVersion;
    private final Map<String, String> headers;
    private final byte[] body;

    private HttpRequest(HttpMethod httpMethod, String path, Map<String, String> queryParams, String protocolVersion,
                        Map<String, String> headers, byte[] body) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryParams = queryParams;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest of(HttpMethod httpMethod, String path, Map<String, String> queryParams,
                                 String protocolVersion, Map<String, String> headers, byte[] body) {
        return new HttpRequest(httpMethod, path, queryParams, protocolVersion, headers, body);
    }

//
//
//    /**
//     * HttpRequest를 통해 HttpResponse를 생성하는 로직
//     *
//     * @return 생성된 HttpRequest
//     * @throws IOException
//     */
//    public HttpResponse createHttpResponse() throws IOException {
//        if (isDynamicHttpRequest()) {
//            return createDynamicHttpResponse();
//        } else {
//            return createStaticHttpResponse();
//        }
//    }
////
////    //path에 ?가 포함되면 동적 요청, 아니라면 정적파일요청
////    private boolean isDynamicHttpRequest() {
////        return this.path.contains("?");
////    }
////
////    private HttpResponse createStaticHttpResponse() throws IOException {
////        String contentType = getContentTypeFromRequestPath();
////        List<String> headers = new ArrayList<>();
////
////        byte[] body = FileMapper.getByteConvertedFile(this.path);
////        headers.add("Content-Type: " + contentType + ";charset=utf-8\r\n");
////        headers.add("Content-Length: " + String.valueOf(body.length) + "\r\n");
////        headers.add("\r\n"); //Blank l
////        return new HttpResponse("HTTP/1.1", HttpStatus.OK, headers, body);
////    }
////
////    private HttpResponse createDynamicHttpResponse() throws UnsupportedEncodingException {
////        List<String> headers = new ArrayList<>();
////        headers.add("Location: /index.html \r\n");
////        String content = path.substring(this.path.indexOf("?") + 1);
////        String[] splitPath = content.split("&");
////        List<String> infos = new ArrayList<>();
////        for (String s : splitPath) {
////            infos.add(URLDecoder.decode(s.split("=")[1], "UTF-8"));
////        }
////
////        User user = new User(infos.get(0), infos.get(1), infos.get(2), infos.get(3));
////        Database.addUser(user);
////        return new HttpResponse("HTTP/1.1", HttpStatus.SEE_OTHER, headers, new byte[0]);
////    }


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
