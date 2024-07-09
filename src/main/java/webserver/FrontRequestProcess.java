package webserver;

import handler.UserHandler;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final UserHandler userHandler;

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
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
                return new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion(), null);
            }
        }

        logger.debug("path: " + path + ", method: " + method);
        return switch (HttpRequestMapper.of(path, method)) {
            case ROOT ->
                    new HttpResponseObject(StringUtil.STATIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion(), null);
            case SIGNUP_REQUEST -> {
                User user = userHandler.create(request.getBodyMap());
                yield new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion(), null);
            }
            case LOGIN_REQUEST ->
                userHandler.login(request.getBodyMap())
                        .map(user -> new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.INDEX_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion(), null))
                        .orElse(new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.LOGIN_FAIL_HTML, HttpCode.FOUND.getStatus(), request.getHttpVersion(), null));
            case USER_LOGIN_FAIL -> {
                byte[] body = IOUtil.readBytesFromFile(false, StringUtil.LOGIN_FAIL_HTML);
                yield new HttpResponseObject(StringUtil.DYNAMIC, StringUtil.LOGIN_FAIL_HTML, HttpCode.OK.getStatus(), request.getHttpVersion(), body);
            }
            case MESSAGE_NOT_ALLOWED ->
                    new HttpResponseObject(StringUtil.FAULT, null, HttpCode.METHOD_NOT_ALLOWED.getStatus(), request.getHttpVersion(), null);
            case ERROR ->
                    new HttpResponseObject(StringUtil.FAULT, null, HttpCode.NOT_FOUND.getStatus(), request.getHttpVersion(), null);
            default ->
                    new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion(), null);
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
        sendBody(dos, response.getBody());
    }

    private void sendHeader(DataOutputStream dos, HttpResponseObject response) throws IOException {
        String path = response.getPath();
        switch (HttpCode.of(response.getStatusCode())) {
            case OK:
                response.addHeader("Content-Type", ContentType.getType(path.contains(StringUtil.DOT) ? path.split(StringUtil.REGDOT)[1] : String.valueOf(ContentType.HTML)));
                response.addHeader("Content-Length", response.getBody().length + "");
                break;
            case FOUND:
                response.addHeader("Location", path);
                break;
            case METHOD_NOT_ALLOWED:
                break;
            case NOT_FOUND:
                break;
            default:
                break;
        }
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
