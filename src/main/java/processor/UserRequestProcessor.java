package processor;

import db.Database;
import exception.StatusCodeException;
import model.User;
import type.MIMEType;
import type.StatusCodeType;
import utils.FileUtils;
import utils.StringUtils;
import webserver.RequestInfo;

import java.io.IOException;
import java.util.HashMap;

public class UserRequestProcessor extends RequestProcessor {

    public UserRequestProcessor(RequestInfo requestInfo) throws StatusCodeException, IOException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /user/login.html" -> loginPage();
            case "GET /user/login_failed.html" -> loginFailedPage();

            case "POST /user/create" -> createUser();
            case "POST /user/login" -> loginUser();
            default -> throw new StatusCodeException(StatusCodeType.NOT_FOUND);
        }
    }

    private void loginPage() throws IOException {
        String path = STATIC_PATH + "/login/login.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        setResult(StatusCodeType.OK, getResponseHeader(), body);
    }

    private void loginFailedPage() throws IOException {
        String path = STATIC_PATH + "/login/login_failed.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        setResult(StatusCodeType.OK, getResponseHeader(), body);
    }

    // POST "/user/create"
    private void createUser() {
        HashMap<String, String> param = StringUtils.param2Map(getBody());
        String userId = param.get("userId");
        String name = param.get("name");
        String password = param.get("password");
        String email = param.get("email");

        if (userId == null || name == null || password == null || email == null ||
                userId.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty()) {

            insertHTMLTypeToHeader();
            setResult(StatusCodeType.BAD_REQUEST, getResponseHeader(), "" +
                    "<script>" +
                    "alert('Fill all the required fields');" +
                    "location.href='/registration'" +
                    "</script>");

            return;
        }

        User user = new User(userId, name, password, email);
        Database.addUser(user);
        insertToResponseHeader("Location", "/user/login.html");
        setResult(StatusCodeType.FOUND, getResponseHeader(), "");
    }

    // POST "/user/login"
    private void loginUser() {
        HashMap<String, String> param = StringUtils.param2Map(getBody());
        String userId = param.get("userId");
        String password = param.get("password");

        try {
            User user = Database.findUserById(userId);
            if (user.getPassword().equals(password)) { // 로그인 성공
                // 세션 생성
                String sessionId = StringUtils.generateRandomString(16);
                Database.addSession(sessionId, userId);

                // 결과값 작성
                insertToResponseHeader("Set-Cookie", "sid=" + sessionId + "; Path=/");
                insertToResponseHeader("Location", "/");
                setResult(StatusCodeType.FOUND, getResponseHeader(), "");
            } else throw new Exception("login failed"); // null exception or 로그인 실패
        } catch (Exception e) {
            insertToResponseHeader("Location", "/user/login_failed.html");
            setResult(StatusCodeType.FOUND, getResponseHeader(), "");
        }
    }
}
