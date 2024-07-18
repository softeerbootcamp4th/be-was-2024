package controller;

import util.*;

import java.io.IOException;

public abstract class AbstractController implements Controller {

    @Override
    public HttpResponse service(HttpRequest request) {
        return switch (request.getRequestMethod().toUpperCase()) {
            case ConstantUtil.GET -> doGet(request);
            case ConstantUtil.POST -> doPost(request);
            default -> HttpResponse.methodNotAllowed(request.getHttpVersion());
        };
    }

    public HttpResponse doGet(HttpRequest request) {
        return HttpResponse.forward(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
    }

    public HttpResponse doPost(HttpRequest request) {
        return HttpResponse.forward(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
    }

    /**
     * 파일을 읽어 String으로 반환하는 메서드 (중복 제거용)
     * @param path
     * @return String
     */
    protected String readBytesFromFile(String path) {
        try {
            return new String(IOUtil.readBytesFromFile(false, path));
        } catch (IOException e) {
            return HttpRequestMapper.DEFAULT_PAGE.getPath();
        }
    }
}
