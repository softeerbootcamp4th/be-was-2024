package webserver;

import java.io.*;
import java.net.Socket;

import common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String dirPath = "/Users/eckrin/study/be-was-2024/src/main/resources/static";

    private Socket connection;
    private WebAdapter webAdapter;

    public RequestHandler(Socket connectionSocket, WebAdapter adapter) {
        this.connection = connectionSocket;
        this.webAdapter = adapter;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str, path = "", extension = "", contentType = "";

            while(!(str = br.readLine()).isEmpty()) {
                String[] headerLine = str.split(" ");
                if(WebUtils.isMethodHeader(headerLine[0])) { // request uri path 추출하기
                    path = headerLine[1];
                    if(WebUtils.isRESTRequest(path)) {
                        path = webAdapter.resolveRequestUri(path);
                        System.out.println("path = " + path);
                    }
                    extension = path.substring(path.lastIndexOf(".")+1);
                    contentType = WebUtils.getProperContentType(extension);
                    readAndResponseFromPath(out, dirPath+path, contentType);
                }
                logger.debug("{}", str);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void readAndResponseFromPath(OutputStream out, String path, String contentType) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);
        File file = new File(path);
        byte[] body = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        }

        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+contentType+";charset=utf-8\r\n");
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
