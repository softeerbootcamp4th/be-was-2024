package controller;

import util.HttpRequest;
import util.HttpRequestMapper;
import util.HttpResponse;

/**
 * Error를 처리하는 컨트롤러, Error을 구체적으로 판별하기 위해 mapper 객체를 내부 필드로 놓고 switch문으로 처리한다.
 */
public class ErrorController extends AbstractController{

    public final HttpRequestMapper mapper;

    public ErrorController(HttpRequestMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 에러 판정 후 대응되는 HttpResponse를 반환한다.
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse service(HttpRequest request) {
        return switch (mapper) {
            case METHOD_NOT_ALLOWED -> HttpResponse.methodNotAllowed(request.getHttpVersion());
            case NOT_FOUND -> HttpResponse.notFound(request.getHttpVersion());
            default -> HttpResponse.sendRedirect(HttpRequestMapper.NOT_FOUND.getPath(), request.getHttpVersion());
        };
    }
}
