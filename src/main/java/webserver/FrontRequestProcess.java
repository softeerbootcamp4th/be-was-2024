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
import java.util.HashMap;
import java.util.Map;

public class FrontRequestProcess {

    private static final Logger logger = LoggerFactory.getLogger(FrontRequestProcess.class);
    private final ModelHandler<User> userHandler;

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    public Map<String, String> handleRequest(HttpRequestObject httpRequestObject) {
        String path = httpRequestObject.getRequestPath();
        String method = httpRequestObject.getRequestMethod();
        // TODO: 추후 기능 확장 시 관심사별로 분리 필요
        Map<String, String> responseInfo = new HashMap<>();
        switch (HttpRequestMapper.of(path, method)) {
            case USER_SIGNUP:
                userHandler.create(httpRequestObject.getRequestParams());
                responseInfo.put("type", "dynamic");
                responseInfo.put("path", "/index.html");
                responseInfo.put("statusCode", "302");
                break;
            case REGISTER:
                responseInfo.put("type", "dynamic");
                responseInfo.put("path", "/registration");
                responseInfo.put("statusCode", "302");
                break;
            default:
                responseInfo.put("type", "static");
                responseInfo.put("path", path);
                responseInfo.put("statusCode", "200");
                break;
        }
        return responseInfo;
    }

    public void handleResponse(OutputStream out, Map<String, String> responseInfo) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        if (responseInfo.get("type").equals("static")) {
            staticResponse(dos, responseInfo.get("path"));
            return;
        }

        switch(responseInfo.get("statusCode")) {
            case "302":
                response302Header(dos, responseInfo.get("path"));
                break;
            default:
                break;
        }
    }

    private void staticResponse(DataOutputStream dos, String path) throws IOException {
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
