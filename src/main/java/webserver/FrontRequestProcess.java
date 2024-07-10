package webserver;

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
        if (path.contains(StringUtil.DOT)) {
            int idx = path.lastIndexOf(StringUtil.DOT);
            if(idx == -1) throw new IllegalArgumentException("Invalid Path: " + path);
            String extension = path.substring(idx + 1);
            if (!extension.equals(ContentType.HTML.getExtension())) {
                return HttpResponse.okStatic(path, HttpCode.OK.getStatus(), request.getHttpVersion());
            }
        }

        // 로그인 및 로그아웃 요청은 별도로 처리
        if(HttpRequestMapper.isAuthRequest(path, method)){
            return handleAuthRequest(request);
        }

        // TODO: 추후 Article, Comment 관련 동작은 별도로 분리하여 매핑테이블 크기 조절 필요
        logger.info("Request Path: {}, Method: {}", path, method);
        HttpRequestMapper mapper = HttpRequestMapper.of(path, method);
        return switch (mapper) {
            case ROOT -> HttpResponse.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            case SIGNUP_REQUEST -> {
                userHandler.create(request.getBodyMap());
                yield HttpResponse.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            }
            case USER_LOGIN_FAIL -> {
                String body = new String(IOUtil.readBytesFromFile(false, StringUtil.LOGIN_FAIL_HTML));
                yield HttpResponse.ok(StringUtil.DYNAMIC, path, mapper.getCode(), request.getHttpVersion(), body);
            }
            case INDEX_HTML -> {
                // 세션ID가 있는 경우 로그인 상태로 간주
                String sessionId = sessionHandler.parseSessionId(request.getRequestHeaders().get(StringUtil.COOKIE)).orElse(null);
                if(sessionId != null){
                    String body = new String(IOUtil.readBytesFromFile(false, StringUtil.INDEX_HTML));
                    yield HttpResponse.ok(StringUtil.DYNAMIC, path, mapper.getCode(), request.getHttpVersion(), body);
                }

                yield HttpResponse.okStatic(path, mapper.getCode(), request.getHttpVersion());
            }
            case MESSAGE_NOT_ALLOWED, ERROR ->
                    HttpResponse.error(mapper.getCode(), request.getHttpVersion());
            default ->
                    HttpResponse.okStatic(path, mapper.getCode(), request.getHttpVersion());
        };
    }

    public HttpResponse handleAuthRequest(HttpRequest request){
        HttpRequestMapper mapper = HttpRequestMapper.of(request.getRequestPath(), request.getRequestMethod());
        if(mapper.equals(HttpRequestMapper.LOGIN_REQUEST)){ // 로그인
            Session session = sessionHandler.login(request.getBodyMap()).orElse(null);
            if (session == null) { // 로그인 실패 시 로그인 실패 페이지로 리다이렉트
                return HttpResponse.redirect(StringUtil.LOGIN_FAIL_HTML, mapper.getCode(), request.getHttpVersion());
            }

            HttpResponse response = HttpResponse.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            response.setSessionId(session.getSessionId());
            return response;
        } else { // 로그아웃
            String sessionId = sessionHandler.parseSessionId(request.getRequestHeaders().get(StringUtil.COOKIE)).orElse(null);
            if(sessionId == null){ // 로그아웃인데 세션ID가 없는 경우 올바르지 않은 요청이므로 리다이렉트
                return HttpResponse.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            }

            sessionHandler.logout(sessionId);
            HttpResponse response = HttpResponse.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            response.deleteSessionId(sessionId);
            return response;
        }
    }

    public void handleResponse(OutputStream out, HttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        // 정적 자원인 경우 바로 반환
        if (response.getType().equals(StringUtil.STATIC)) {
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
        String extension = isDir ? ContentType.HTML.getExtension() : path.split(StringUtil.REGDOT)[1];
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + StringUtil.CRLF);
            dos.writeBytes("Content-Type: " + ContentType.getType(extension) + StringUtil.CRLF);
            dos.writeBytes("Content-Length: " + body.length + StringUtil.CRLF);
            dos.writeBytes(StringUtil.CRLF);
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
