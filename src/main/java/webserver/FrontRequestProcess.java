package webserver;

import exception.RequestException;
import handler.SessionHandler;
import handler.ModelHandler;
import handler.UserHandler;
import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

        // 로그인 상태인지 확인하기 위해 세션 ID 선 추출
        String sessionId = sessionHandler.parseSessionId(request.getRequestHeaders().get(ConstantUtil.COOKIE)).orElse(null);

        // TODO: 추후 Article, Comment 관련 동작은 별도로 분리하여 매핑테이블 크기 조절 필요
        logger.info("Request Path: {}, Method: {}", path, method);
        HttpRequestMapper mapper = HttpRequestMapper.of(path, method);
        return switch (mapper) {
            case ROOT -> HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            case SIGNUP, LOGIN, LOGIN_FAIL -> {
                String body = new String(IOUtil.readBytesFromFile(false, path));
                yield HttpResponse.ok(ConstantUtil.DYNAMIC, path, request.getHttpVersion(), body);
            }
            case SIGNUP_REQUEST -> {
                userHandler.create(request.getBodyMap());
                yield HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            }
            case USER_LIST -> {
                path += ConstantUtil.DOT_HTML;
                // 세션ID가 있는 경우 [총 사용자 목록 출력]
                if (sessionId != null) {
                    String body = DynamicHtmlUtil.generateUserListHtml(path, userHandler.findAll().stream().toList());
                    yield HttpResponse.ok(ConstantUtil.DYNAMIC, path, request.getHttpVersion(), body);
                }

                // 세션ID가 없다면 [로그인 페이지로 이동] -> 성공
                yield HttpResponse.redirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());
            }
            case INDEX_HTML -> {
                // 세션ID가 있는 경우 로그인 상태로 간주하여 [사용자 이름] 표시
                String body = new String(IOUtil.readBytesFromFile(false, path));
                if (sessionId != null) {
                    Session session = sessionHandler.findSessionById(sessionId);
                    body = body.replace(DynamicHtmlUtil.USER_NAME_TAG, DynamicHtmlUtil.generateUserIdHtml(session.getUserId()));
                    yield HttpResponse.ok(ConstantUtil.DYNAMIC, path, request.getHttpVersion(), body);
                }

                // 세션ID가 없다면 [로그인 버튼] 표시
                body = body.replace(DynamicHtmlUtil.LOGIN_BUTTON_TAG, DynamicHtmlUtil.LOGIN_BUTTON_HTML);
                yield HttpResponse.ok(ConstantUtil.DYNAMIC, path, request.getHttpVersion(), body);
            }
            case MESSAGE_NOT_ALLOWED -> HttpResponse.error(HttpCode.METHOD_NOT_ALLOWED.getStatus(), request.getHttpVersion());
            case NOT_FOUND -> HttpResponse.error(HttpCode.NOT_FOUND.getStatus(), request.getHttpVersion());
            default -> HttpResponse.okStatic(path, request.getHttpVersion());
        };
    }

    public HttpResponse handleAuthRequest(HttpRequest request){
        HttpRequestMapper mapper = HttpRequestMapper.of(request.getRequestPath(), request.getRequestMethod());
        if(mapper.equals(HttpRequestMapper.LOGIN_REQUEST)){ // 로그인
            Session session = sessionHandler.login(request.getBodyMap()).orElse(null);
            if (session == null) { // 로그인 실패 시 로그인 실패 페이지로 리다이렉트
                return HttpResponse.redirect(HttpRequestMapper.LOGIN_FAIL.getPath(), request.getHttpVersion());
            }

            HttpResponse response = HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            response.setSessionId(session.getSessionId());
            return response;
        } else { // 로그아웃
            String sessionId = sessionHandler.parseSessionId(request.getRequestHeaders().get(ConstantUtil.COOKIE)).orElse(null);
            if(sessionId == null){ // 로그아웃인데 세션ID가 없는 경우 올바르지 않은 요청이므로 리다이렉트
                return HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            }

            sessionHandler.logout(sessionId);
            HttpResponse response = HttpResponse.redirect(HttpRequestMapper.INDEX_HTML.getPath(), request.getHttpVersion());
            response.deleteSessionId(sessionId);
            return response;
        }
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
        String extension = isDir ? ContentType.HTML.getExtension() : path.split(ConstantUtil.REGDOT)[1];
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
}
