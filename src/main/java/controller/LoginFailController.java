package controller;

import util.HttpRequest;
import util.HttpRequestMapper;
import util.HttpResponse;

/**
 * 로그인 실패 관련 요청을 처리
 */
public class LoginFailController extends AbstractController{

    /**
     * 로그인 실패 화면을 불러옴
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doGet(HttpRequest request) {
        String path = HttpRequestMapper.LOGIN_FAIL.getPath();
        return HttpResponse.forward(path, request.getHttpVersion(), readBytesFromFile(path));
    }
}
