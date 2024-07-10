package webserver;

import handler.AuthHandler;
import handler.ModelHandler;
import handler.UserHandler;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final ModelHandler<User> userHandler;
    private final AuthHandler authHandler;

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
        this.authHandler = AuthHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    public HttpResponseObject handleRequest(HttpRequestObject request) throws IOException {
        String path = request.getRequestPath();
        String method = request.getRequestMethod();

        // css, img 등 html이 아닌 정적 자원인 경우 즉시 반환
        if (path.contains(StringUtil.DOT)) {
            String extension = path.split(StringUtil.REGDOT)[1];
            if (!extension.equals(ContentType.HTML.getExtension())) {
                return HttpResponseObject.okStatic(path, HttpCode.OK.getStatus(), request.getHttpVersion());
            }
        }

        // 로그인 및 로그아웃 요청은 별도로 처리
        if(HttpRequestMapper.isAuthRequest(path, method)){
            return handleAuthRequest(request);
        }

        // TODO: 추후 Article, Comment 관련 동작은 별도로 분리하여 매핑테이블 크기 조절 필요
        // TODO: Status Code enum 클래스에 편입시켜서 중복 코드 줄여보기, 그리고 Response 객체 팩토리메서드 추가하기
        logger.info("Request Path: {}, Method: {}", path, method);
        HttpRequestMapper mapper = HttpRequestMapper.of(path, method);
        return switch (mapper) {
            case ROOT -> HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            case SIGNUP_REQUEST -> {
                userHandler.create(request.getBodyMap());
                yield HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            }
            case USER_LOGIN_FAIL -> {
                String body = new String(IOUtil.readBytesFromFile(false, StringUtil.LOGIN_FAIL_HTML));
                yield HttpResponseObject.ok(StringUtil.DYNAMIC, path, mapper.getCode(), request.getHttpVersion(), body);
            }
            case INDEX_HTML -> {
                // 세션ID로 유저를 찾았다 -> templates/index.html 반환
                if(request.getRequestHeaders().get(StringUtil.COOKIE) != null){
                    String sessionId = CookieManager.parseUserCookie(request.getRequestHeaders().get(StringUtil.COOKIE));
                    if(authHandler.findUserBySessionId(sessionId).isPresent()){
                        String body = new String(IOUtil.readBytesFromFile(false, StringUtil.INDEX_HTML));
                        yield HttpResponseObject.ok(StringUtil.DYNAMIC, path, mapper.getCode(), request.getHttpVersion(), body);
                    }
                }

                yield HttpResponseObject.okStatic(path, mapper.getCode(), request.getHttpVersion());
            }
            case MESSAGE_NOT_ALLOWED, ERROR ->
                    HttpResponseObject.error(mapper.getCode(), request.getHttpVersion());
            default ->
                    HttpResponseObject.okStatic(path, mapper.getCode(), request.getHttpVersion());
        };
    }

    public HttpResponseObject handleAuthRequest(HttpRequestObject request){
        HttpRequestMapper mapper = HttpRequestMapper.of(request.getRequestPath(), request.getRequestMethod());
        if(mapper.equals(HttpRequestMapper.LOGIN_REQUEST)){ // Login
            Optional<User> user = authHandler.login(request.getBodyMap());
            if (user.isEmpty()) {
                return HttpResponseObject.redirect(StringUtil.LOGIN_FAIL_HTML, mapper.getCode(), request.getHttpVersion());
            }

            HttpResponseObject response = HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            CookieManager.setUserCookie(response, user.get());
            return response;
        } else { // Logout: 클라이언트의 쿠키 중 sid를 무력화시키고 static/index.html로 리다이렉트
            if(request.getRequestHeaders().get(StringUtil.COOKIE) == null)
                return HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());

            String sessionId = CookieManager.parseUserCookie(request.getRequestHeaders().get(StringUtil.COOKIE));
            authHandler.logout(sessionId);
            HttpResponseObject response = HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
            CookieManager.deleteUserCookie(response, sessionId);
            return HttpResponseObject.redirect(StringUtil.INDEX_HTML, mapper.getCode(), request.getHttpVersion());
        }
    }

    public void handleResponse(OutputStream out, HttpResponseObject response) throws IOException {
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
