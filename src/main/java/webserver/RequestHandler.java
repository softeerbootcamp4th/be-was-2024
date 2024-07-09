package webserver;

import common.WebUtils;
import exception.SizeNotMatchException;
import file.ViewFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpRequest;
import web.HttpResponse;
import web.ResponseCode;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String STATIC_DIR_PATH = "./src/main/resources/static";
    private final String DYNAMIC_DIR_PATH = "./src/main/resources/template";
    private final int BUFFER_SIZE = 4096;

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

            // 요청 헤더를 파싱하여 Request객체 생성
            HttpRequest httpRequest = parseHttpRequest(in);
            // Request객체의 정보를 바탕으로 적절한 응답 뷰 파일 탐색
            ViewFile viewFile = ViewResolver.getProperFileFromRequest(httpRequest, out);
            // 뷰 파일에 맞게 적절한 Response객체 생성
            HttpResponse httpResponse = WebAdapter.createResponse(ResponseCode.OK, WebUtils.getProperContentType(viewFile.getExtension()));
            // Stream을 이용하여 HTTP 응답
            readAndResponseFromPath(out, STATIC_DIR_PATH +viewFile.getPath(), httpResponse.getContentType());

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
     * InputStream에서 Request를 String형태로 읽어온 후, HttpRequest를 만들어 반환한다.
     * body는 byte[]형태로 받아온다.
     */
    private HttpRequest parseHttpRequest(InputStream in) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        int contentLength = 0;
        byte[] body = null;

        // Request Line부터 Body 전까지 읽는다
        // Request Line과 Body 사이에는 '\r\n'이 두번 오게 된다.
        boolean lastWasCR = false;
        boolean lastWasLF = false;
        while((ch = in.read())!=-1) {
            sb.append((char) ch);

            if(ch=='\r') {
                lastWasCR = true;
            } else if (ch=='\n') {
                if(lastWasCR && lastWasLF) {
                    break;
                }
                lastWasLF = true;
            } else {
                lastWasCR = false;
                lastWasLF = false;
            }
        }

        // Content-Length 헤더 찾기
        String header = sb.toString();
        String[] headerLines = header.split("\r\n");
        for (String line : headerLines) {
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
                break;
            }
        }

        // Content-Length가 0보다 크다면 body를 읽는다.
        if(contentLength>0) {
            body = new byte[contentLength];
            int readSize = in.read(body, 0, contentLength);
            if(readSize!=contentLength) {
                throw new SizeNotMatchException("Content-Length 크기와 읽은 body 사이즈가 다릅니다");
            }
            sb.append(new String(body));
            sb.append("\n");
        }

        logger.debug("{}", sb);

        return WebAdapter.parseRequest(sb.toString(), body);
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
            responseHeader(ResponseCode.OK, dos, body.length, contentType);
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
