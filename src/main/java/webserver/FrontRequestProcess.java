package webserver;

import handler.ModelHandler;
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
    private final ModelHandler<User> userHandler;

    private FrontRequestProcess(){
        this.userHandler = UserHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    public void handleRequest(OutputStream out, HttpRequestObject httpRequestObject) throws IOException {
        String path = httpRequestObject.getRequestPath();
        String method = httpRequestObject.getRequestMethod();
        // TODO: 추후 기능 확장 시 관심사별로 분리 필요
        switch(HttpRequestMapper.of(path, method)){
            case USER_SIGNUP:
                userHandler.create(httpRequestObject.getRequestParams());
                response302Header(new DataOutputStream(out), "/index.html");
                break;
            case REGISTER:
                response302Header(new DataOutputStream(out), "/registration");
                break;
            default:
                staticResponse(out, path);
                break;
        }
    }

    private void staticResponse(OutputStream out, String path) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = IOUtil.readBytesFromFile(IOUtil.STATIC_PATH + path);
        boolean isDir = IOUtil.isDirectory(IOUtil.STATIC_PATH + path);
        response200Header(dos, body.length, isDir ? "html" : path.split("\\.")[1]);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            logger.debug("200 OK 응답을 보냅니다.");
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + ContentType.getType(extension) + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            logger.debug("302 리다이렉트 발생하여 {}로 이동합니다.", location);
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
