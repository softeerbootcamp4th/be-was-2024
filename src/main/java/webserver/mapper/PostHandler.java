package webserver.mapper;

import db.BoardDatabase;
import db.Session;
import model.*;
import webserver.HttpRequest;
import webserver.HttpResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Post 요청에 대한 정보를 처리해주는 클래스
 */
public class PostHandler {

    private static String redirectUrl = "/index.html";
    private static String sessionId;

    /**
     * Post 요청이 들어왔을 시 주소 정보에 따른 처리를 담당하는 클래스
     * @param httpRequest 요청되어온 정보를 담은 객체
     * @param httpResponse 응답 보낼 정보를 담은 객체
     * @throws IOException
     */
    public static void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String url = httpRequest.getUrl();
        byte[] body = httpRequest.getBody();
        Map<String, String> headers = httpRequest.getHeaders();
        User user = null;
        try {
            switch (url) {
                case "/user/create":
                    UserCreate.createUser(new String(body, "UTF-8"));
                    httpResponse.redirectPath(redirectUrl);
                    break;
                case "/login":
                    user = UserLogin.login(new String(body, "UTF-8"));
                    if(user == null){
                        httpResponse.redirectPath(redirectUrl);
                        break;
                    }
                    redirectUrl = "/main/index.html";
                    sessionId = SessionIdControl.createSessionId(user);
                    httpResponse.setCookieAndRedirectPath(sessionId, redirectUrl);
                    break;
                case "/logout":
                    sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("cookie"));
                    SessionIdControl.deleteSessionId(sessionId);
                    redirectUrl = "/index.html";
                    httpResponse.resetCookieAndRedirectPath(redirectUrl);
                    break;
                case "/board/create":
                    sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("cookie"));
                    user = Session.findUserBySessionId(sessionId);
                    String content = new String(body, "UTF-8").split("=")[1];
                    BoardDatabase.addBoard(new Board(user.getUserId(), content));
                    redirectUrl = "/main/index.html";
                    httpResponse.redirectPath(redirectUrl);
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            httpResponse.sendErrorPage("Invalid information has been entered", redirectUrl);
        }

    }

}
