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
                return new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion());
            }
        }

        // TODO: 추후 Article, Comment 관련 동작은 별도로 분리하여 매핑테이블 크기 조절 필요
        logger.info("Request Path: {}, Method: {}", path, method);
        return switch (HttpRequestMapper.of(path, method)) {
            case ROOT ->
                    new HttpResponseObject(StringUtil.STATIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
            case SIGNUP_REQUEST -> {
                User user = userHandler.create(request.getBodyMap());
                yield new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
            }
            case LOGIN_REQUEST -> {
                Optional<User> user = authHandler.login(request.getBodyMap());
                if (user.isEmpty()) {
                    yield new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.LOGIN_FAIL_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
                }

                HttpResponseObject response = new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
                CookieManager.setUserCookie(response, user.get());
                yield response;
            }
            case LOGOUT_REQUEST -> {
                // 클라이언트의 쿠키 중 sid를 무력화시키고 static index.html로 리다이렉트
                if(request.getRequestHeaders().get("Cookie") != null) {
                    String sessionId = CookieManager.parseUserCookie(request.getRequestHeaders().get("Cookie"));
                    authHandler.logout(sessionId);

                    HttpResponseObject response = new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
                    CookieManager.deleteUserCookie(response, sessionId);
                    yield response;
                }

                yield new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion());
            }
            case USER_LOGIN_FAIL -> {
                // 로그인 실패 시 로그인 실패 페이지 반환
                HttpResponseObject response = new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.LOGIN_FAIL_HTML, HttpCode.OK.getStatus(), request.getHttpVersion());
                response.putBody(new String(IOUtil.readBytesFromFile(false, StringUtil.LOGIN_FAIL_HTML)));
                yield response;
            }
            case INDEX_HTML -> {
                // 세션ID로 유저를 찾았다 -> templates/index.html 반환
                if(request.getRequestHeaders().get("Cookie") != null){
                    String sessionId = CookieManager.parseUserCookie(request.getRequestHeaders().get("Cookie"));
                    if(authHandler.findUserBySessionId(sessionId).isPresent()){
                        HttpResponseObject response = new HttpResponseObject(StringUtil.DYNAMIC, path, HttpCode.OK.getStatus(), request.getHttpVersion());
                        response.putBody(new String(IOUtil.readBytesFromFile(false, StringUtil.INDEX_HTML)));
                        yield response;
                    }
                }

                yield new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion());
            }
            case MESSAGE_NOT_ALLOWED ->
                    new HttpResponseObject(StringUtil.FAULT, null, HttpCode.METHOD_NOT_ALLOWED.getStatus(), request.getHttpVersion());
            case ERROR ->
                    new HttpResponseObject(StringUtil.FAULT, null, HttpCode.NOT_FOUND.getStatus(), request.getHttpVersion());
            default ->
                    new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion());
        };
    }

    public void handleResponse(OutputStream out, HttpResponseObject response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        // 정적 자원인 경우 바로 반환
        if (response.getType().equals(StringUtil.STATIC)) {
            staticResponse(dos, response.getPath());
            return;
        }

        sendHeader(dos, response);
        if(response.getBody() != null)
            sendBody(dos, response.getBody());
    }

    private void sendHeader(DataOutputStream dos, HttpResponseObject response) throws IOException {
        String path = response.getPath();
        switch (HttpCode.of(response.getStatusCode())) {
            case OK:
                response.putHeader("Content-Type", ContentType.getType(path.contains(StringUtil.DOT) ? path.split(StringUtil.REGDOT)[1] : String.valueOf(ContentType.HTML)));
                response.putHeader("Content-Length", response.getBody().length + "");
                break;
            case FOUND:
                response.putHeader("Location", path);
                break;
            case METHOD_NOT_ALLOWED:
                break;
            case NOT_FOUND:
                break;
            default:
                break;
        }
        logger.info(response.getTotalHeaders());
        dos.writeBytes(response.getTotalHeaders());
    }

    private void sendBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
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
