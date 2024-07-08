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

    private FrontRequestProcess() {
        this.userHandler = UserHandler.getInstance();
    }

    public static FrontRequestProcess getInstance() {
        return FrontRequestProcess.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final FrontRequestProcess INSTANCE = new FrontRequestProcess();
    }

    public HttpResponseObject handleRequest(HttpRequestObject request) {
        String path = request.getRequestPath();
        String method = request.getRequestMethod();
        return switch (HttpRequestMapper.of(path, method)) {
            case USER_SIGNUP -> {
                userHandler.create(request.getRequestBody());
                yield new HttpResponseObject(StringUtil.DYNAMIC, "/index.html", HttpCode.FOUND.getStatus(), request.getHttpVersion(), null);
            }
            case REGISTER -> new HttpResponseObject(StringUtil.DYNAMIC, "/registration", HttpCode.FOUND.getStatus(), request.getHttpVersion(), null);
            case MESSAGE_NOT_ALLOWED -> new HttpResponseObject(StringUtil.FAULT, "/405.html", HttpCode.METHOD_NOT_ALLOWED.getStatus(), request.getHttpVersion(), null);
            default -> new HttpResponseObject(StringUtil.STATIC, path, HttpCode.OK.getStatus(), request.getHttpVersion(), null);
        };
    }

    public void handleResponse(OutputStream out, HttpResponseObject response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        if (response.getType().equals(StringUtil.STATIC)) {
            staticResponse(dos, response.getPath());
            return;
        }
        sendHeader(dos, response);
        sendBody(dos, response.getBodyToByte());
    }

    private void sendHeader(DataOutputStream dos, HttpResponseObject response) throws IOException{
        String path = response.getPath();
        switch(HttpCode.of(response.getStatusCode())){
            case OK:
                response.addHeader("Content-Type", ContentType.getType(path.contains(StringUtil.DOT) ? path.split(StringUtil.DOT)[1] : String.valueOf(ContentType.HTML)));
                response.addHeader("Content-Length", response.getBodyToByte().length + "");
                break;
            case FOUND:
                response.addHeader("Location", path);
                break;
            case METHOD_NOT_ALLOWED:
                break;
            default:
                break;
        }
        dos.writeBytes(response.getTotalHeaders());
    }

    private void sendBody(DataOutputStream dos, byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void staticResponse(DataOutputStream dos, String path) throws IOException {
        byte[] body = IOUtil.readBytesFromFile(IOUtil.STATIC_PATH + path);
        boolean isDir = IOUtil.isDirectory(IOUtil.STATIC_PATH + path);
        String extension = isDir ? ContentType.HTML.getExtension() : path.split(StringUtil.DOT)[1];
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
