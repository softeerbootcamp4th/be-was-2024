package controller;

import exception.RequestException;
import session.Session;
import session.SessionHandler;
import util.*;

/**
 * 로그아웃 요청을 처리
 */
public class LogoutController extends AbstractController{

    /**
     * 세션을 삭제하고 로그아웃 처리한다.
     * @param request
     * @return HttpResponse
     */
    @Override
    public HttpResponse doPost(HttpRequest request) {
        Session session = request.getSession();
        if(session == null){
            throw new RequestException(ConstantUtil.INVALID_HEADER);
        }

        HttpResponse response = HttpResponse.sendRedirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        String sessionId = session.getSessionId();
        SessionHandler.getInstance().logout(sessionId);
        response.deleteSessionId(sessionId);
        return response;
    }
}
