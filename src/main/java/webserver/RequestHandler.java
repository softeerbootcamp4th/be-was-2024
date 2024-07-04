package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.HttpRequestObject;
import util.ContentType;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final UserRequestProcess userRequestProcess;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.userRequestProcess = UserRequestProcess.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            // 요청 Header 출력
            String line = br.readLine(); // Request Line (ex: "GET /index.html HTTP/1.1")
            HttpRequestObject httpRequestObject = HttpRequestObject.from(line);
            while(!line.isEmpty()) {
                logger.debug("header: {}", line);
                line = br.readLine();
            }

            frontRequestProcess(out, httpRequestObject);
        }  catch (IOException e) {
            logger.error("error: {}", e.getStackTrace());
        }
    }

    private void frontRequestProcess(OutputStream out, HttpRequestObject httpRequestObject) throws IOException {
        String path = httpRequestObject.getRequestPath();
        if(path.equals("/user/create")) {
            userRequestProcess.createUser(httpRequestObject);
            dynamicResponse(out, httpRequestObject);
        }
        else {
            staticResponse(out, httpRequestObject);
        }
    }

    private void dynamicResponse(OutputStream out, HttpRequestObject httpRequestObject) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = FileUtil.readBytesFromFile(FileUtil.STATIC_PATH + "/index.html");
        response200Header(dos, body.length, ContentType.HTML.getExtension());
        responseBody(dos, body);
    }

    private void staticResponse(OutputStream out, HttpRequestObject httpRequestObject) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = FileUtil.readBytesFromFile(FileUtil.STATIC_PATH + httpRequestObject.getRequestPath());
        boolean isDir = FileUtil.isDirectory(FileUtil.STATIC_PATH + httpRequestObject.getRequestPath());
        response200Header(dos, body.length, isDir ? ContentType.HTML.getExtension() : httpRequestObject.getRequestPath().split("\\.")[1]);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + ContentType.getType(extension) + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
