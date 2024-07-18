package controller;

import session.Session;
import session.SessionHandler;
import util.HttpRequest;
import util.HttpRequestMapper;
import util.HttpResponse;

import java.util.Optional;

/**
 * 로그인 관련 요청 처리
 */
public class LoginController extends AbstractController {

    /**
     * 로그인 화면을 불러옴
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doGet(HttpRequest request) {
        String path = HttpRequestMapper.LOGIN.getPath();
        return HttpResponse.forward(path, request.getHttpVersion(), readBytesFromFile(path));
    }

    /**
     * 로그인 요청을 처리
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doPost(HttpRequest request) {
        Optional<Session> session = SessionHandler.getInstance().login(request.getBodyMap());
        if (session.isEmpty()) {
            return HttpResponse.sendRedirect(HttpRequestMapper.LOGIN_FAIL.getPath(), request.getHttpVersion());
        }

        HttpResponse response = HttpResponse.sendRedirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        response.setSessionId(session.get().toString());
        return response;
    }
}
