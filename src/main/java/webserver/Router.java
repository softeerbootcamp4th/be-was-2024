package webserver;

import enums.Status;
import model.Article;
import model.ArticleDao;
import model.User;
import model.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Handler> getHandlersMap = new HashMap<>();
    private final Map<String, Handler> postHandlersMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    private final UserDao userDao = new UserDao();
    private final ArticleDao articleDAO = new ArticleDao();
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
        getHandlersMap.put("/article", this::getArticlePage);
    }

    private void initPostHandlers() {
        postHandlersMap.put("/user/create", this::createUserHandler);
        postHandlersMap.put("/login", this::loginHandler);
        postHandlersMap.put("/logout", this::logoutHandler);
        postHandlersMap.put("/article/create", this::createArticleHandler);
    }

    private String createUserHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        httpResponseHandler.setStatus(Status.FOUND);
        Map<String, String> formData = getFormData(httpRequestParser);

        String userId = URLDecoder.decode(formData.get("userId"), "UTF-8");
        String password = URLDecoder.decode(formData.get("password"), "UTF-8");
        String name = URLDecoder.decode(formData.get("name"), "UTF-8");
        String email = URLDecoder.decode(formData.get("email"), "UTF-8");

        try {
            if (userDao.userExists(userId)) {
                model.addAttribute("errorMessage", "이미 가입된 회원입니다.");
                return "/registration/index";
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        User user = new User(userId, password, name, email);

        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return "redirect:/";
    }

    private String createArticleHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) {
        httpResponseHandler.setStatus(Status.FOUND);

        String sessionId = httpRequestParser.getCookiesMap().get("sid");
        User user = SessionManager.getUser(sessionId);

        String content = httpRequestParser.getParsedContent();
        byte[] image = httpRequestParser.getParsedImage();

        try {
            articleDAO.addArticle(new Article(user.getUserId(), content, image));
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return "redirect:/";
    }


    private String getMainPage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        httpResponseHandler.setStatus(Status.OK);

        String sessionId = httpRequestParser.getCookiesMap().get("sid");

        // 모든 게시글 가져와서 {{postList}}에 뿌려주기
        StringBuilder postListHtml = new StringBuilder();
        try {
            Collection<Article> articles = articleDAO.getAllArticles();
            for (Article article : articles) {
                String decodedContent = URLDecoder.decode(article.getContent(), "UTF-8");
                String base64Image = Base64.getEncoder().encodeToString(article.getImage());

                postListHtml.append("<div class=\"post\">\n");
                postListHtml.append("<div class=\"post__account\">\n");
                postListHtml.append("<img class=\"post__account__img\"/>\n");
                postListHtml.append("<p class=\"post__account__nickname\">").append(article.getAuthor()).append("</p>\n");
                postListHtml.append("</div>\n");
                postListHtml.append("<img class=\"post__img\" src=\"data:image/jpeg;base64,").append(base64Image).append("\" alt=\"Image\" />\n");
                postListHtml.append("<div class=\"post__menu\">\n");
                postListHtml.append("<ul class=\"post__menu__personal\">\n");
                postListHtml.append("<li><button class=\"post__menu__btn\"><img src=\"./img/like.svg\" /></button></li>\n");
                postListHtml.append("<li><button class=\"post__menu__btn\"><img src=\"./img/sendLink.svg\" /></button></li>\n");
                postListHtml.append("</ul>\n");
                postListHtml.append("<button class=\"post__menu__btn\"><img src=\"./img/bookMark.svg\" /></button>\n");
                postListHtml.append("</div>\n");
                postListHtml.append("<p class=\"post__article\">").append(decodedContent).append("</p>\n");
                postListHtml.append("</div>\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("postList", postListHtml.toString());

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
            try {
                Collection<User> userList = userDao.getAllUsers();
                StringBuilder userListBuilder = new StringBuilder();
                for (User user : userList) {
                    userListBuilder.append("<tr>")
                            .append("<td>").append(user.getUserId()).append("</td>")
                            .append("<td>").append(user.getName()).append("</td>")
                            .append("<td>").append(user.getEmail()).append("</td>")
                            .append("</tr>");
                }

                model.addAttribute("userList", userListBuilder.toString());
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }

            // 유저 정보를 HTML 테이블 행으로 변환
            return "/user_list";
        } else {
            httpResponseHandler.setStatus(Status.FOUND);
            return "redirect:/login";
        }
    }

    private String getArticlePage(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler, Model model) throws IOException {
        String sessionId = httpRequestParser.getCookiesMap().get("sid");

        if (SessionManager.isValidSession(sessionId)) {
            httpResponseHandler.setStatus(Status.OK);
            return "/article/index";
        } else {
            httpResponseHandler.setStatus(Status.FOUND);
            return "redirect:/login";
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

        String userId = URLDecoder.decode(formData.get("userId"), "UTF-8");
        String password = URLDecoder.decode(formData.get("password"), "UTF-8");

        User user = null;

        try {
            user = userDao.getUser(userId);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

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
            SessionManager.removeSession(sessionId);
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
