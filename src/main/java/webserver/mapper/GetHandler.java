package webserver.mapper;

import db.Session;
import model.User;
import model.UserInfoExtract;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Get 요청에 대한 정보를 처리해주는 클래스
 */
public class GetHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    /**
     * Get 요청이 들어왔을 시 주소 정보에 따른 처리를 담당하는 클래스
     * @param httpRequest 요청되어온 정보를 담은 객체
     * @param httpResponse 응답 보낼 정보를 담은 객체
     * @throws IOException
     */
    public static synchronized void handle(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String url = httpRequest.getUrl();
        Map<String, String> headers = httpRequest.getHeaders();
        String sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("cookie"));
        User user = Session.findUserBySessionId(sessionId);
        try {
            switch (url){
                case "/registration":
                    url = staticResourceDir + "/registration/index.html";
                    break;
                case "/login":
                    url = staticResourceDir + "/login/index.html";
                    break;
                case "/main/index.html":
                    httpResponse.showBoard(user);
                    return;
                case "/user/list":
                    if(Session.findUserBySessionId(sessionId) == null){
                        url = staticResourceDir + "/login/index.html";
                        break;
                    }
                    httpResponse.openUserList();
                    return;
                case "/write":
                    if(Session.findUserBySessionId(sessionId) == null){
                        url = staticResourceDir + "/login/index.html";
                        break;
                    }
                    url = staticResourceDir + "/article/index.html";
                    break;
                case "/index.html":
                    httpResponse.showBoard(user);
                    return;
                default:
                    url = staticResourceDir + url;
                    break;
            }

            httpResponse.openPath(url);

        }catch (Exception e){
            httpResponse.sendErrorPage("Invalid information has been entered", "/index.html");
        }
    }
}
