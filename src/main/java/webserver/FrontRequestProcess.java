package webserver;

import exception.RequestException;
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

public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final ModelHandler<User> userHandler;
    private final SessionHandler sessionHandler;

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
        this.sessionHandler = SessionHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    public HttpResponse handleRequest(HttpRequest request) throws IOException {
        String path = request.getRequestPath();
        String method = request.getRequestMethod();

        // css, img 등 html이 아닌 정적 자원인 경우 즉시 반환
        if (path.contains(ConstantUtil.DOT)) {
            int idx = path.lastIndexOf(ConstantUtil.DOT);
            if(idx == -1) throw new RequestException(ConstantUtil.INVALID_PATH + path);
            String extension = path.substring(idx + 1);
            if (!extension.equals(ContentType.HTML.getExtension())) {
                return HttpResponse.okStatic(path, request.getHttpVersion());
            }
        }

        // 로그인 및 로그아웃 요청은 별도로 처리
        if(HttpRequestMapper.isAuthRequest(path, method)){
            return handleAuthRequest(request);
        }

        // TODO: 추후 Article, Comment 관련 동작은 별도로 분리하여 매핑테이블 크기 조절 필요
        return switch (HttpRequestMapper.of(path, method)) {
            case ROOT -> HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            case INDEX_HTML -> handleIndexRequest(path, request.getRequestHeaders().get(ConstantUtil.COOKIE), request.getHttpVersion());
            case SIGNUP, LOGIN, LOGIN_FAIL -> HttpResponse.ok(ConstantUtil.DYNAMIC, path, request.getHttpVersion(), readBytesFromFile(path));
            case SIGNUP_REQUEST -> {
                userHandler.create(request.getBodyMap());
                yield HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            }
            case USER_LIST -> handleUserListRequest(path, request.getRequestHeaders().get(ConstantUtil.COOKIE), request.getHttpVersion());
            case MESSAGE_NOT_ALLOWED -> HttpResponse.error(HttpCode.METHOD_NOT_ALLOWED.getStatus(), request.getHttpVersion());
            case NOT_FOUND -> HttpResponse.error(HttpCode.NOT_FOUND.getStatus(), request.getHttpVersion());
            default -> HttpResponse.okStatic(path, request.getHttpVersion());
        };
    }

    // 로그인 및 로그아웃 요청 처리 및 세션 관리
    public HttpResponse handleAuthRequest(HttpRequest request){
        HttpRequestMapper mapper = HttpRequestMapper.of(request.getRequestPath(), request.getRequestMethod());
        if(mapper.equals(HttpRequestMapper.LOGIN_REQUEST)){ // 로그인 성공 시 세션ID 반환, 실패 시 로그인 실패 페이지로 리다이렉트
            return sessionHandler.login(request.getBodyMap())
                    .map(session -> {
                        HttpResponse response = HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
                        response.setSessionId(session.toString());
                        return response;
                    })
                    .orElseGet(() -> HttpResponse.redirect(HttpRequestMapper.LOGIN_FAIL.getPath(), request.getHttpVersion()));
        } else { // // 로그아웃 시 세션 ID를 추출하여 세션 삭제, 세션이 없는데 로그아웃 요청이 들어온 경우 리다이렉트
            return sessionHandler.parseSessionId(request.getRequestHeaders().get(ConstantUtil.COOKIE))
                    .map(sessionId -> {
                        sessionHandler.logout(sessionId);
                        HttpResponse response = HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
                        response.deleteSessionId(sessionId);
                        return response;
                    })
                    .orElseGet(() -> HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion()));
        }
    }

    // 세션ID가 있는 경우 [총 사용자 목록 출력], 없다면 [로그인 페이지로 이동]
    public HttpResponse handleUserListRequest(String path, String cookieLine, String httpVersion) throws IOException{
        String pathWithHtml = path + ConstantUtil.DOT_HTML;
        String body = readBytesFromFile(pathWithHtml);
        return sessionHandler.parseSessionId(cookieLine)
                .map(sessionId -> {
                    List<User> users = userHandler.findAll().stream().toList();
                    String bodyWithUserList = body.replace(DynamicHtmlUtil.USER_LIST_TAG, DynamicHtmlUtil.generateUserListHtml(users));
                    return HttpResponse.ok(ConstantUtil.DYNAMIC, pathWithHtml, httpVersion, bodyWithUserList);
                })
                .orElseGet(() -> HttpResponse.redirect(HttpRequestMapper.LOGIN.getPath(), httpVersion));
    }

    // 세션ID가 있는 경우 로그인 상태로 간주하여 [사용자 ID] 표시, 없다면 [로그인 버튼] 표시
    public HttpResponse handleIndexRequest(String path, String cookieLine, String httpVersion) throws IOException {
        String body = readBytesFromFile(path);
        return sessionHandler.parseSessionId(cookieLine)
                .map(sessionId -> {
                    String userId = sessionHandler.findSessionById(sessionId)
                            .map(Session::getUserId)
                            .orElseThrow(() -> new RequestException(ConstantUtil.INVALID_SESSION));
                    String bodyWithUser = body.replace(DynamicHtmlUtil.USER_NAME_TAG, DynamicHtmlUtil.generateUserIdHtml(userId));
                    return HttpResponse.ok(ConstantUtil.DYNAMIC, path, httpVersion, bodyWithUser);
                })
                .orElseGet(() -> {
                    String bodyWithLogin = body.replace(DynamicHtmlUtil.LOGIN_BUTTON_TAG, DynamicHtmlUtil.LOGIN_BUTTON_HTML);
                    return HttpResponse.ok(ConstantUtil.DYNAMIC, path, httpVersion, bodyWithLogin);
                });
    }

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

    private String readBytesFromFile(String path) throws IOException {
        return new String(IOUtil.readBytesFromFile(false, path));
    }
}
