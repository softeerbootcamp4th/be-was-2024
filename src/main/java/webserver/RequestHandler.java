package webserver;

import file.ViewFile;
import web.HttpRequest;
import common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpResponse;
import web.ResponseCode;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String dirPath = "./src/main/resources/static";

    private Socket connection;

    private RequestHandler() {
    }

    private static class InnerInstanceClass {
        private static final RequestHandler instance = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return InnerInstanceClass.instance;
    }

    public void setConnection(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            StringBuilder requestString = readHttpRequest(in);
            logger.debug("{}", requestString);

            // 요청 헤더를 파싱하여 Request객체 생성
            HttpRequest httpRequest = WebAdapter.parseRequest(requestString.toString());
            // Request객체의 정보를 바탕으로 적절한 응답 뷰 파일 탐색
            ViewFile viewFile = ViewResolver.getProperFileFromRequest(httpRequest, out);
            // 뷰 파일에 맞게 적절한 Response객체 생성
            HttpResponse httpResponse = WebAdapter.createResponse(ResponseCode.SUCCESS, WebUtils.getProperContentType(viewFile.getExtension()));
            // Stream을 이용하여 HTTP 응답
            readAndResponseFromPath(out, dirPath+viewFile.getPath(), httpResponse.getContentType());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * InputStream에서 Request를 String형태로 읽어온다.
     */
    private StringBuilder readHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String str;
        StringBuilder sb = new StringBuilder();
        int contentLength = 0;

        while((str = br.readLine())!=null && !str.isEmpty()) {
            if(str.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(str.split(":")[1].trim());
            }
            sb.append(str).append("\n");
        }

        // body가 존재한다면 읽는다.
        if(contentLength>0) {
            char[] body = new char[contentLength];
            br.read(body, 0, contentLength);
            sb.append(body);
        }

        return sb;
    }

    /**
     * 경로에서 적절한 뷰 파일을 찾아서 응답합니다.
     */
    private void readAndResponseFromPath(OutputStream out, String path, String contentType) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);
        File file = new File(path);
        byte[] body = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
            responseHeader(ResponseCode.SUCCESS, dos, body.length, contentType);
            responseBody(dos, body);
        } catch (Exception e) {
            responseHeader(ResponseCode.INTERNAL_SERVER_ERROR, dos, body.length, contentType);
            responseBody(dos, body);
            e.printStackTrace();
        }
    }

    private void responseHeader(ResponseCode code, DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 "+code.getCode()+" OK \r\n");
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
