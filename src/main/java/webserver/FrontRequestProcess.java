package webserver;

import controller.Controller;
import exception.ModelException;
import exception.RequestException;
import session.SessionHandler;
import session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 요청을 처리하고 그에 맞는 응답을 생성하는 클래스
 */
public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final SessionHandler sessionHandler;

    private FrontRequestProcess() {
        this.sessionHandler = SessionHandler.getInstance();
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
    public HttpResponse handleRequest(HttpRequest request) {
        try {
            String path = request.getRequestPath();
            String method = request.getRequestMethod();

            // css, img 등 html이 아닌 정적 자원인 경우 즉시 반환
            if (path.contains(ConstantUtil.DOT)) {
                int idx = path.lastIndexOf(ConstantUtil.DOT);
                if (idx == -1) throw new RequestException(ConstantUtil.INVALID_PATH + path);
                String extension = path.substring(idx + 1);
                if (!extension.equals(ContentType.HTML.getExtension())) {
                    return HttpResponse.forward(path, request.getHttpVersion());
                }
            }

            // 세션이 유효한지 검사하고 세션 객체 반환
            Session session = StringParser.parseSessionId(request.getHeader(ConstantUtil.COOKIE))
                    .flatMap(sessionHandler::findSessionById)
                    .orElse(null);

            // 세션이 존재하나 유효하지 않은 경우 세션 삭제하고 로그인 페이지로 리다이렉트
            if (session != null && !sessionHandler.validateSession(session)) {
                return HttpResponse.sendRedirect(HttpRequestMapper.LOGIN.getPath(), request.getHttpVersion());
            }
            request.putSession(session);

            // 요청에 대응되는 Controller를 찾아서 요청 처리하고 응답 객체 반환
            Controller controller = HttpRequestMapper.getController(path, method);
            return controller.service(request);
        } catch (RequestException | ModelException e) {
            logger.error(e.getMessage());
            return HttpResponse.sendRedirect(HttpRequestMapper.DEFAULT_PAGE.getPath(), request.getHttpVersion());
        }
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
}
