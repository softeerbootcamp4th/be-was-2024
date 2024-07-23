package webserver;

import exception.ModelException;
import exception.RequestException;
import handler.ArticleHandler;
import model.Article;
import session.SessionHandler;
import handler.ModelHandler;
import handler.UserHandler;
import session.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 요청을 처리하고 그에 맞는 응답을 생성하는 클래스
 */
public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final ModelHandler<User> userHandler;
    private final ModelHandler<Article> articleHandler;
    private final SessionHandler sessionHandler;

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
        this.sessionHandler = SessionHandler.getInstance();
        this.articleHandler = ArticleHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    /**
     * path와 method를 추출해 이를 기반으로 요청을 처리하고 응답을 생성하는 메서드
     * @param request
     * @return HttpResponse
     */
    public HttpResponse handleRequest(HttpRequest request) throws IOException {
        try {
            String path = request.getRequestPath();
            String method = request.getRequestMethod();

            // css, img 등 html이 아닌 정적 자원인 경우 즉시 반환
            if (path.contains(ConstantUtil.DOT)) {
                int idx = path.lastIndexOf(ConstantUtil.DOT);
                if (idx == -1) throw new RequestException(ConstantUtil.INVALID_PATH + path);
                String extension = path.substring(idx + 1);
                if (!extension.equals(ContentType.HTML.getExtension())) {
                    return HttpResponse.okStatic(path, request.getHttpVersion());
                }
            }

            // 로그인 및 로그아웃 요청은 별도로 처리
            if (HttpRequestMapper.isAuthRequest(path, method)) {
                return handleAuthRequest(request);
            }

            // 세션이 유효한지 검사하고 세션 객체 반환
            Session session = sessionHandler.parseSessionId(request.getRequestHeaders().get(ConstantUtil.COOKIE))
                    .flatMap(sessionHandler::findSessionById)
                    .orElse(null);

            // 세션이 존재하나 유효하지 않은 경우 세션 삭제하고 로그인 페이지로 리다이렉트
            if (session != null && !sessionHandler.validateSession(session)) {
                return HttpResponse.redirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());
            }

            return switch (HttpRequestMapper.of(path, method)) {
                case ROOT -> HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
                case DEFAULT_PAGE -> handleIndexRequest(path, request, session);
                case ARTICLE, ARTICLE_CREATE -> handleArticleRequest(path, request, session);
                case USER_LIST -> handleUserListRequest(path, request, session);
                case SIGNUP_REQUEST -> handleSignUpRequest(request);
                case SIGNUP, LOGIN, LOGIN_FAIL ->
                        HttpResponse.ok(path, request.getHttpVersion(), readBytesFromFile(path));
                case METHOD_NOT_ALLOWED -> HttpResponse.methodNotAllowed(request.getHttpVersion());
                case NOT_FOUND -> HttpResponse.notFound(request.getHttpVersion());
                default ->
                        HttpResponse.redirect(HttpRequestMapper.NOT_FOUND.getPath(), request.getHttpVersion()); // 요청 경로가 없는 경우
            };
        } catch (ModelException e){
            logger.debug(e.getMessage());
            return HttpResponse.error(e.getMessage(), request.getHttpVersion());
        } catch (RequestException e){
            logger.debug(e.getMessage());
            return HttpResponse.error(e.getMessage(), request.getHttpVersion());
        }
    }

    /**
     * 게시물에 대한 요청을 처리하며 세션이 없으면 로그인 페이지로 리다이렉트
     * @param path
     * @param request
     * @param session
     * @return HttpResponse
     */
    private HttpResponse handleArticleRequest(String path, HttpRequest request, Session session) {
        if(session == null)
            return HttpResponse.redirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());

        return switch (HttpRequestMapper.of(path, request.getRequestMethod())) {
            case ARTICLE -> HttpResponse.okStatic(path, request.getHttpVersion());
            case ARTICLE_CREATE -> {
                User user = userHandler.findById(session.getUserId()).orElseThrow(() -> new ModelException(ConstantUtil.USER_NOT_FOUND));
                Map<String, String> fields = request.getBodyMap();
                fields.put(ConstantUtil.AUTHOR_NAME,  user.getName()); // authorName 추가
                articleHandler.create(fields);
                yield HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
            }
            default -> HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        };
    }

    /**
     * 회원가입 요청을 처리하며 회원가입 성공 시 로그인 페이지로 리다이렉트, 실패 시 회원가입 페이지로 리다이렉트
     * @param request
     * @return HttpResponse
     */
    private HttpResponse handleSignUpRequest(HttpRequest request){
        try {
            userHandler.create(request.getBodyMap());
            return HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        } catch (ModelException e) {
            return HttpResponse.redirect(HttpRequestMapper.REGISTER.getPath(), request.getHttpVersion());
        }
    }

    /**
     * 로그인 및 로그아웃 요청 처리 및 세션 관리,
     * 로그인 성공 시 세션 ID 반환, 실패 시 로그인 실패 페이지로 리다이렉트
     * 로그아웃 시 세션 ID를 추출하여 세션 삭제, 세션이 없는데 로그아웃 요청이 들어온 경우 리다이렉트
     * @param request
     * @return HttpResponse
     */
    private HttpResponse handleAuthRequest(HttpRequest request){
        HttpRequestMapper mapper = HttpRequestMapper.of(request.getRequestPath(), request.getRequestMethod());
        if(mapper.equals(HttpRequestMapper.LOGIN_REQUEST)){ // 로그인 성공 시 세션ID 반환, 실패 시 로그인 실패 페이지로 리다이렉트
            return sessionHandler.login(request.getBodyMap())
                    .map(session -> {
                        HttpResponse response = HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
                        response.setSessionId(session.toString());
                        return response;
                    })
                    .orElseGet(() -> HttpResponse.redirect(HttpRequestMapper.LOGIN_FAIL.getPath(), request.getHttpVersion()));
        } else { // // 로그아웃 시 세션 ID를 추출하여 세션 삭제, 세션이 없는데 로그아웃 요청이 들어온 경우 리다이렉트
            return sessionHandler.parseSessionId(request.getRequestHeaders().get(ConstantUtil.COOKIE))
                    .map(sessionId -> {
                        sessionHandler.logout(sessionId);
                        HttpResponse response = HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
                        response.deleteSessionId(sessionId);
                        return response;
                    })
                    .orElseGet(() -> HttpResponse.redirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion()));
        }
    }

    /**
     * 사용자 목록 조회 요청을 처리, 세션ID가 있는 경우에만 총 사용자 목록을 출력하며 없다면 로그인 페이지로 이동
     * @param path
     * @param request
     * @param session
     * @return HttpResponse
     */
    private HttpResponse handleUserListRequest(String path, HttpRequest request, Session session) throws IOException{
        if(session == null)
            return HttpResponse.redirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());

        String pathWithHtml = path + ConstantUtil.DOT_HTML;
        String body = readBytesFromFile(pathWithHtml);
        List<User> users = userHandler.findAll();
        String bodyWithUserList = body.replace(DynamicHtmlUtil.USER_LIST_TAG, DynamicHtmlUtil.generateUserListHtml(users));
        return HttpResponse.ok(pathWithHtml, request.getHttpVersion(), bodyWithUserList);
    }

    /**
     * index.html 요청을 처리, 세션ID가 있는 경우 로그인 상태로 간주하여 사용자 ID 표시, 없다면 로그인 버튼 표시
     * @param path
     * @param request
     * @param session
     * @return HttpResponse
     */
    private HttpResponse handleIndexRequest(String path, HttpRequest request, Session session) throws IOException {
        String body = readBytesFromFile(path);
        if(session == null)
            return HttpResponse.ok(path, request.getHttpVersion(), body);

        String bodyWithData = body.replace(DynamicHtmlUtil.USER_NAME_TAG, DynamicHtmlUtil.generateUserIdHtml(session.getUserId())); // 사용자 ID 표시
        bodyWithData = bodyWithData.replace(DynamicHtmlUtil.LOGIN_BUTTON_TAG, DynamicHtmlUtil.LOGIN_BUTTON_INVISIBLE); // 로그인 버튼 비활성화
        bodyWithData = bodyWithData.replace(DynamicHtmlUtil.ARTICLES_TAG, DynamicHtmlUtil.generateArticlesHtml(articleHandler.findAll())); // 게시글 목록 표시
        return HttpResponse.ok(path, request.getHttpVersion(), bodyWithData);
    }

    /**
     * 응답을 실제로 처리하는 메서드, 완성된 응답을 클라이언트에 DataOutputStream을 통해 전송
     * @param out
     * @param response
     */
    public void handleResponse(OutputStream out, HttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        // 정적 자원인 경우 바로 반환
        if (response.getType().equals(ConstantUtil.STATIC)) {
            staticResponse(dos, response.getPath());
            return;
        }

        dos.writeBytes(response.getTotalHeaders());
        byte[] body = response.getBody();
        if(body != null) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }

    /**
     * 정적인 응답을 처리하는 메서드
     * @param dos
     * @param path
     */
    private void staticResponse(DataOutputStream dos, String path) throws IOException {
        byte[] body = IOUtil.readBytesFromFile(true, path);
        boolean isDir = IOUtil.isDirectory(true, path);
        String[] element = path.split(ConstantUtil.REGDOT);
        String extension = isDir ? ContentType.HTML.getExtension() : element[element.length - 1];
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + ConstantUtil.CRLF);
            dos.writeBytes("Content-Type: " + ContentType.getType(extension) + ConstantUtil.CRLF);
            dos.writeBytes("Content-Length: " + body.length + ConstantUtil.CRLF);
            dos.writeBytes(ConstantUtil.CRLF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 파일을 읽어 String으로 반환하는 메서드 (중복 제거용)
     * @param path
     * @return String
     */
    private String readBytesFromFile(String path) throws IOException {
        return new String(IOUtil.readBytesFromFile(false, path));
    }
}
