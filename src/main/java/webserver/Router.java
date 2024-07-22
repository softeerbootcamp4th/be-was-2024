package webserver;

import db.Database;
import enums.Status;
import model.User;
import utils.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Handler> getHandlersMap = new HashMap<>();
    private final Map<String, Handler> postHandlersMap = new HashMap<>();
    public Router() {
        initGetHandlers();
        initPostHandlers();
    }

    public Handler getHandler(HttpRequestParser httpRequestParser) throws IOException {
        Handler handler = null;

        switch (httpRequestParser.getMethod()) {
            case GET -> handler = getHandlersMap.get(httpRequestParser.getPath());
            case POST -> handler = postHandlersMap.get(httpRequestParser.getPath());
        }

        if (handler != null) {
            return handler;
        }

        return this::getInvalidPage;
    }

    private void initGetHandlers() {
        getHandlersMap.put("/registration", this::getRegistrationPage);
        getHandlersMap.put("/", this::getMainPage);
        getHandlersMap.put("/login", this::getLoginPage);
        getHandlersMap.put("/main", this::getMainPage);
        getHandlersMap.put("/user/list", this::getUserListPage);
    }

    private void initPostHandlers() {
        postHandlersMap.put("/user/create", this::createUserHandler);
        postHandlersMap.put("/login", this::loginHandler);
        postHandlersMap.put("/logout", this::logoutHandler);
    }

    private String createUserHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) {
        httpResponseHandler.setStatus(Status.FOUND);
        Map<String, String> formData = getFormData(httpRequestParser);

        String userId = formData.get("userId");
        String password = formData.get("password");
        String name = formData.get("name");
        String email = formData.get("email");

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        return "redirect:/";
    }

    private String getMainPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        httpResponseHandler.setStatus(Status.OK);

        String sessionId = httpRequestParser.getCookiesMap().get("sid");

        if (SessionManager.isValidSession(sessionId)) {
            // 로그인 한 유저
            User user = SessionManager.getUser(sessionId);

            model.addAttribute("userName",
                    "<li class=\"header__menu__item\">\n" +
                            "              " + user.getName() + "\n" +
                            "          </li>"
            );
            model.addAttribute("loginSection",
                    "          <li class=\"header__menu__item\">\n" +
                            "            <form id=\"logout-form\" action=\"/logout\" method=\"POST\" style=\"display: inline;\">\n" +
                            "              <button id=\"logout-btn\" class=\"btn btn_contained btn_size_s\" type=\"submit\">\n" +
                            "                로그아웃\n" +
                            "              </button>\n" +
                            "            </form>\n" +
                            "          </li>\n"
            );

            model.addAttribute("registration", "");

        } else {
            // 로그인 안한 유저
            model.addAttribute("userName", "");
            model.addAttribute("loginSection", "<a class=\"btn btn_contained btn_size_s\" href=\"/login\">로그인</a>");
            model.addAttribute("registration",
                    "<li class=\"header__menu__item\">\n" +
                            "            <a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">\n" +
                            "              회원 가입\n" +
                            "            </a>\n" +
                            "          </li>"
            );
        }

        return "/index";
    }

    private String getUserListPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        String sessionId = httpRequestParser.getCookiesMap().get("sid");

        if (SessionManager.isValidSession(sessionId)) {
            httpResponseHandler.setStatus(Status.OK);
            Collection<User> userList = Database.findAll();
            StringBuilder userListBuilder = new StringBuilder();
            for (User user : userList) {
                userListBuilder.append("<tr>")
                        .append("<td>").append(user.getUserId()).append("</td>")
                        .append("<td>").append(user.getName()).append("</td>")
                        .append("<td>").append(user.getEmail()).append("</td>")
                        .append("</tr>");
            }

            model.addAttribute("userList", userListBuilder.toString());

            // 유저 정보를 HTML 테이블 행으로 변환
            return "/user_list";

        } else {
            httpResponseHandler.setStatus(Status.NOT_FOUND);
            return "/invalid";
        }


    }

    private String getLoginPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        httpResponseHandler.setStatus(Status.OK);

        return "/login/index";
    }

    private String getRegistrationPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        httpResponseHandler.setStatus(Status.OK);

        return  "/registration/index";
    }

    private String loginHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        Map<String, String> formData = getFormData(httpRequestParser);

        String userId = formData.get("userId");
        String password = formData.get("password");

        User user = Database.findUserById(userId);

        // 로그인 성공
        if (user != null && user.getPassword().equals(password)) {
            String sessionId = SessionManager.createSession(user);
            Cookie cookie = new Cookie.Builder("sid", sessionId)
                    .path("/")
                    .build();

            httpResponseHandler
                    .setStatus(Status.FOUND)
                    .addCookie(cookie);

            return "redirect:/";

        } else {
            // 로그인 실패
            httpResponseHandler.setStatus(Status.UNAUTHORIZED);

            return "/login/login_failed";
        }
    }

    private String logoutHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) {
        httpResponseHandler.setStatus(Status.FOUND);
        Map<String, String> cookiesMap = httpRequestParser.getCookiesMap();
        String sessionId = cookiesMap.get("sid");
        if (sessionId != null) {
            Cookie cookie = new Cookie.Builder("sid", sessionId)
                    .path("/")
                    .maxAge(0)
                    .build();

            httpResponseHandler.addCookie(cookie);
        }

        return "redirect:/";
    }

    private String getInvalidPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) {
        httpResponseHandler.setStatus(Status.NOT_FOUND);

        return "/invalid";
    }

    private Map<String, String> getFormData(HttpRequestParser httpRequestParser) {
        String body = new String(httpRequestParser.getBody());
        Map<String, String> formData = new HashMap<>();
        for (String keyValue : body.split("&")) {
            int equalsIndex = keyValue.indexOf("=");
            if (equalsIndex != -1) {
                formData.put(keyValue.substring(0, equalsIndex), keyValue.substring(equalsIndex + 1));
            }
        }

        return formData;
    }
}
