package webserver.api.login;

import model.post.PostDAO;
import model.user.User;
import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.session.SessionDAO;
import webserver.util.HtmlFiles;
import webserver.http.response.PageBuilder;
import webserver.util.ParamsParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;


/**
 * login이 유효한지 확인하는 클래스
 */
public class LoginHandler implements FunctionHandler {
    // Singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LoginHandler();

        return single_instance;
    }

    /**
     * login이 유효한지 확인한다
     * <p>
     *     아이디 및 비밀번호가 유효하다면 main page로 redirect 한다
     * </p>
     * <p>
     *     유효하지 않다면 오류 페이지를 띄운다
     * </p>
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     * @see UserDAO
     * @see SessionDAO
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String body = new String(request.getBody(), StandardCharsets.UTF_8);
        Map<String, String> params = ParamsParser.parseParams(body);
        UserDAO userDAO = new UserDAO();

        String id =params.get("id");
        String password = params.get("password");
        User user = userDAO.getUser(id);

        // if user information is not valid
        if(id == null || id.isEmpty()
                || password == null || password.isEmpty()
                || user == null || !Objects.equals(user.getPassword(), password)){
            return new HttpResponse.ResponseBuilder(401)
                    .addheader("Content-Type", Extension.HTML.getContentType())
                    .setBody(HtmlFiles.readHtmlByte(HtmlFiles.LOGIN_FAILED))
                    .build();
        }

        // if user information is valid
        SessionDAO sessionDAO = new SessionDAO();
        sessionDAO.deleteSessionByUserid(user.getUserId());
        String sessionString = sessionDAO.insertSession(user.getUserId());

        //go to logined main page
        return new HttpResponse.ResponseBuilder(302)
                .addheader("Content-Type", Extension.HTML.getContentType())
                .addheader("Location", "http://localhost:8080/")
                .addheader("Set-Cookie","sid="+sessionString +"; Max-Age=3600; Path=/") //set cookie
                .build();
    }
}
