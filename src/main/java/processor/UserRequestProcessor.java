package processor;

import db.Database;
import exception.StatusCodeException;
import model.User;
import type.HTTPStatusCode;
import utils.FileUtils;
import utils.StringUtils;
import webserver.RequestInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;

public class UserRequestProcessor extends RequestProcessor {

    public UserRequestProcessor(RequestInfo requestInfo) throws StatusCodeException, IOException {
        init(requestInfo);

        switch (methodPath()) {
            case "GET /user/login.html" -> loginPage();
            case "GET /user/login_failed.html" -> loginFailedPage();
            case "GET /user/list" -> userListPage();

            case "POST /user/create" -> createUser();
            case "POST /user/login" -> loginUser();
            case "POST /user/logout" -> logoutUser();
            default -> throw new StatusCodeException(HTTPStatusCode.NOT_FOUND);
        }
    }

    private void loginPage() throws IOException {
        String path = STATIC_PATH + "/login/login.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        setResult(HTTPStatusCode.OK, getResponseHeader(), body);
    }

    private void loginFailedPage() throws IOException {
        String path = STATIC_PATH + "/login/login_failed.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        setResult(HTTPStatusCode.OK, getResponseHeader(), body);
    }

    private void userListPage() throws IOException {
        HashMap<String, String> cookie = getCookie();
        String sid = cookie.get("sid");

        if (sid == null || Database.findUserIdBySessionId(sid) == null) {
            insertToResponseHeader("Location", "/user/login.html");
            setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
            return;
        }

        String path = STATIC_PATH + "/user/list.html";
        byte[] body = FileUtils.readFileToBytes(path);

        insertHTMLTypeToHeader();
        HashMap<String, String> bodyParams = new HashMap<>();
        StringBuilder userListContent = new StringBuilder();
        String listContent = "<div class=\"user-list-content\">\n" +
                "                <div>ID: {0}</div>\n" +
                "                <div>NAME: {1}</div>\n" +
                "                <div>EMAIL: {2}</div>\n" +
                "            </div>";
        for (User user : Database.findAll()) {
            userListContent.append(MessageFormat.format(listContent, user.getUserId(), user.getName(), user.getEmail()));
        }
        bodyParams.put("userList", userListContent.toString());
        setResult(HTTPStatusCode.OK, getResponseHeader(), body, bodyParams);
    }

    // POST "/user/create"
    private void createUser() throws UnsupportedEncodingException {
        HashMap<String, String> param = StringUtils.paramToMap(getBody(), "&");
        String userId = param.get("userId");
        String name = param.get("name");
        String password = param.get("password");
        String email = param.get("email");

        if (userId == null || name == null || password == null || email == null ||
                userId.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty()) {

            insertHTMLTypeToHeader();
            setResult(HTTPStatusCode.BAD_REQUEST, getResponseHeader(), "" +
                    "<script>" +
                    "alert('Fill all the required fields');" +
                    "location.href='/registration'" +
                    "</script>");

            return;
        }

        if (Database.findUserById(userId) != null) {
            insertHTMLTypeToHeader();
            setResult(HTTPStatusCode.BAD_REQUEST, getResponseHeader(), "" +
                    "<script>" +
                    "alert('이미 등록된 아이디입니다.');" +
                    "location.href='/registration'" +
                    "</script>");

            return;
        }

        User user = new User(userId, password, URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(email, "UTF-8"));
        Database.addUser(user);
        insertToResponseHeader("Location", "/user/login.html");
        setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
    }

    // POST "/user/login"
    private void loginUser() {
        HashMap<String, String> param = StringUtils.paramToMap(getBody(), "&");
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
                setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
            } else throw new Exception("login failed"); // null exception or 로그인 실패
        } catch (Exception e) {
            insertToResponseHeader("Location", "/user/login_failed.html");
            setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
        }
    }

    private void logoutUser() {
        HashMap<String, String> cookie = getCookie();
        String sid = cookie.get("sid");

        insertToResponseHeader("Location", "/");
        if (sid != null && !sid.isEmpty()) {
            Database.removeSession(sid);
            insertToResponseHeader("Set-Cookie", "sid=; Path=/; Max-Age=0");
        }
        setResult(HTTPStatusCode.FOUND, getResponseHeader(), "");
    }
}
