package controller;

import util.*;

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
}
