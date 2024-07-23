package handler;

import db.ArticleDatabase;
import db.UserDatabase;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.Article;
import model.User;
import util.TemplateEngine;
import util.Utils.ResponseWithStatus;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static db.SessionDatabase.*;
import static util.Constants.*;
import static util.Utils.*;

/**
 * GET 요청을 처리하는 핸들러입니다.
 */
public class GetHandler {
    private static final ArticleDatabase articleDatabase = new ArticleDatabase();
    private static final UserDatabase userDatabase = new UserDatabase();

    /**
     * 정적 파일을 반환하는 메서드입니다.
     * @param requestUrl 요청하고자 하는 정적 파일의 경로입니다.
     * @return 해당 정적 파일을 담은 HttpResponse 객체를 반환합니다.
     * @throws IOException
     */
    public static HttpResponse serveStaticFile(String requestUrl) throws IOException {
        String[] tokens = requestUrl.split(REG_DOT);
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(STATIC_PATH + requestUrl);

        byte[] body = responseWithStatus.body;

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader(CONTENT_TYPE, getContentType(type))
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    /**
     * 동적 파일을 반환하는 메서드입니다.
     * @param requestUrl 요청하고자 하는 동적 파일의 경로입니다.
     * @param data 동적 파일에 삽입하고자 하는 데이터를 담은 Map<String, String>입니다.
     * @return 해당 동적 파일을 담은 HttpResponse 객체를 반환합니다.
     * @throws IOException
     */
    public static HttpResponse serveDynamicFile(String requestUrl, Map<String, String> data) throws IOException {
        String[] tokens = requestUrl.split(REG_DOT);
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(STATIC_PATH + requestUrl);
        byte[] body = TemplateEngine.renderTemplate(responseWithStatus.body, data);

        return new HttpResponse()
                .addStatus(responseWithStatus.status)
                .addHeader(CONTENT_TYPE, getContentType(type))
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    /**
     * 루트 페이지를 반환하는 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return <pre>
     *     로그인한 유저가 요청한 경우, 유저의 이름과 로그아웃, 글쓰기 버튼을 담은 루트 파일을 동적으로 렌더링합니다. 비로그인 유저가 요청한 경우 로그인, 회원가입 버튼을 담은 루트 파일을 동적으로 렌더링합니다.
     *     렌더링된 동적 파일을 HttpResponse 객체에 담아 반환합니다.
     *     </pre>
     * @throws IOException
     */
    public static HttpResponse serveRootPage(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        Map<String, String> data = new HashMap<>();
        if (!isLogin(sid)) {
            data.put(USER_NAME, "");
            data.put("msg", "");
            data.put("LoginOrArticle", "/login");
            data.put("loginButton", "로그인");
            data.put("RegistrationOrLogout", "/registration");
            data.put("registrationButton", "회원가입");
        } else {
            String userId = getUser(sid);
            Optional<User> user = userDatabase.findUserById(userId);
            data.put(USER_NAME, URLDecoder.decode(user.get().getName(), "UTF-8"));
            data.put("msg", "님, 환영합니다.");
            data.put("LoginOrArticle", "/article");
            data.put("loginButton", "글쓰기");
            data.put("RegistrationOrLogout", "/logout");
            data.put("registrationButton", "로그아웃");
        }

        data.put("article", getAllArticles());
        return serveDynamicFile(FILE_INDEX, data);
    }

    /**
     * <pre>
     *     유저 로그아웃을 처리하는 메서드입니다.
     *     HttpRequest에서 Session Id를 추출하여 세션 데이터베이스에서 해당 세션을 삭제합니다.
     * </pre>
     *
     * @param httpRequest Http 요청을 담은 httpRequest 객체
     * @return
     */

    public static HttpResponse logout(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        deleteSession(sid);

        byte[] body = new byte[0];

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader(LOCATION, PATH_ROOT)
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addHeader(CONTENT_LENGTH, String.valueOf(body.length))
                .addBody(body);
    }

    /**
     * <pre>
     *     전체 유저 목록을 조회하는 메서드입니다.
     *     비로그인 유저가 접근 시 로그인 페이지로 리다이렉션합니다.
     *     로그인한 유저가 접근 시 전체 유저 목록을 반환합니다.
     * </pre>
     * @param httpRequest Http 요청을 담은 HttpRequest입니다.
     * @return 전체 유저 리스트를 담은 html 파일을 동적으로 렌더링하여 HttpResponse 객체를 통해 반환합니다.
     * @throws IOException
     */
    public static HttpResponse getUserList(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        if (!isLogin(sid)) {
            return new HttpResponse()
                    .addStatus(HttpStatus.FOUND)
                    .addHeader(LOCATION, PATH_LOGIN)
                    .addHeader(CONTENT_TYPE, TEXT_HTML)
                    .addBody(new byte[0]);
        } else {
            Collection<User> users = userDatabase.findAll();

            StringBuilder userList = new StringBuilder();
            for (User user : users) {
                userList.append(TABLE_ROW_START);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getUserId(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getName(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_DATA_START).append(URLDecoder.decode(user.getEmail(), "UTF-8")).append(TABLE_DATA_END);
                userList.append(TABLE_ROW_END);
            }

            Map<String, String> data = new HashMap<>();
            data.put(USER_LIST, userList.toString());
            data.put(USER_NAME, URLDecoder.decode(userDatabase.findUserById(getUser(sid)).get().getName(), "UTF-8"));

            return serveDynamicFile(PATH_USER + FILE_USER_LIST, data);
        }
    }

    /**
     * <pre>
     *     게시글 작성 페이지 요청 메서드입니다.
     *     로그인하지 않은 사용자가 요청 시 로그인 페이지로 리다이렉션합니다.
     * </pre>
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return 게시글 작성 페이지를 동적으로 렌더링하여 HttpResponse 객체를 통해 반환합니다.
     * @throws IOException
     */
    public static HttpResponse postArticle(HttpRequest httpRequest) throws IOException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);
        String userId = getUser(sid);

        if (!isLogin(sid)) {
            return serveStaticFile(PATH_LOGIN + FILE_INDEX);
        } else {
            HashMap<String, String> data = new HashMap<>();
            data.put("userName", URLDecoder.decode(userDatabase.findUserById(userId).get().getName(), "UTF-8"));
            data.put("msg", "님, 환영합니다.");

            return serveDynamicFile(PATH_ARTICLE + FILE_INDEX, data);
        }
    }

    /**
     * Article 데이터베이스에 저장된 모든 게시글을 동적으로 렌더링하여 반환합니다.
     * @return 전체 게시글의 작성자의 이름, 게시글의 본문, 게시글의 이미지를 렌더링하여 반환합니다.
     * @throws IOException
     */
    public static String getAllArticles() throws IOException {
        StringBuilder articles = new StringBuilder();
        List<Article> allArticles = articleDatabase.getAllArticles();
        for (Article article : allArticles) {
            HashMap<String, String> data = new HashMap<>();
            String userName = article.getUserName();
            String text = article.getText().replaceAll("\r\n", "<br>");
            // TODO: 웹에서 <br> 태그 잘 안씀

            String pic = Base64.getEncoder().encodeToString(article.getPic());

            data.put("userName", URLDecoder.decode(userName, "UTF-8"));
            data.put("text", text);
            data.put("base64Image", pic);

            byte[] bytes = TemplateEngine.renderTemplate(getFileContent(STATIC_PATH + "/article.html").body, data);

            articles.append(new String(bytes));
        }
        return articles.toString();
    }
}
