package model;

import db.Database;
import model.enums.HttpStatus;
import util.FileMapper;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class HttpRequest {
    private String httpMethod;
    private String path;
    private String protocolVersion;
    private List<String> headers;

    // 생성자로 사용해도 되는가, 컨버컨버터로 사용해여  하눈거
    public HttpRequest(String httpMethod, String path, String protocolVersion, List<String> headers) {

        this.httpMethod = httpMethod;
        this.path = path;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
    }

    /**
     * HTTP request의 path를 이용하여 확장자를 알아내는 로직
     * Request path를 .으로 분리 한뒤, 마지막 리스트 값을 가져옴.
     * https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     *
     * @return : HttpRequest에 대한 응답헤더의 Content-type
     */
    private String getContentTypeFromRequestPath() {
        String[] splitPath = this.path.split("\\.");
        if (splitPath.length == 0) {
            return "text/plain";
        }
        String extension = splitPath[splitPath.length - 1];

        return switch (extension) {
            case "html" -> "text/html";
            case "svg" -> "image/svg+xml";
            case "jpg" -> "image/jpeg";
            case "js" -> "application/javascript";
            case "css" -> "text/css";
            case "gif" -> "image/gif";
            case "ico" -> "image/vnd.microsoft.icon";
            default -> "text/plain";
        };
    }


    /**
     * HttpRequest를 통해 HttpResponse를 생성하는 로직
     * @return 생성된 HttpRequest
     * @throws IOException
     */
    public HttpResponse createHttpResponse() throws IOException {
        if (isDynamicHttpRequest()) {
            return createDynamicHttpResponse();
        } else {
            return createStaticHttpResponse();
        }
    }

    //path에 ?가 포함되면 동적 요청, 아니라면 정적파일요청
    private boolean isDynamicHttpRequest() {
        return this.path.contains("?");
    }

    private HttpResponse createStaticHttpResponse() throws IOException {
        String contentType = getContentTypeFromRequestPath();
        List<String> headers = new ArrayList<>();

        byte[] body = FileMapper.getByteConvertedFile(this.path);
        headers.add("Content-Type: " + contentType + ";charset=utf-8\r\n");
        headers.add("Content-Length: " + String.valueOf(body.length) + "\r\n");
        headers.add("\r\n"); //Blank l
        return new HttpResponse("HTTP/1.1", HttpStatus.OK, headers, body);
    }

    private HttpResponse createDynamicHttpResponse() throws UnsupportedEncodingException {
        List<String> headers = new ArrayList<>();
        headers.add("Location: /index.html \r\n");
        String content = path.substring(this.path.indexOf("?") + 1);
        String[] splitPath = content.split("&");
        List<String> infos = new ArrayList<>();
        for (String s : splitPath) {
            infos.add(URLDecoder.decode(s.split("=")[1], "UTF-8"));
        }

        User user = new User(infos.get(0), infos.get(1), infos.get(2), infos.get(3));
        Database.addUser(user);
        return new HttpResponse("HTTP/1.1", HttpStatus.SEE_OTHER, headers, new byte[0]);
    }

}
