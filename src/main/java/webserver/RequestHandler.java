package webserver;

import common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final String dirPath = "./src/main/resources/static";

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
            System.out.println("\n====REQUEST====");

            while(!(str = br.readLine()).isEmpty()) {
                logger.debug("{}", str);
                String[] headerLine = str.split(" ");
                if(WebUtils.isMethodHeader(headerLine[0])) { // request method 헤더에 request uri path가 존재하므로
                    path = headerLine[1]; // request uri path 추출하기
                    // rest요청일 경우 - 적절한 view를 찾거나, 적절한 비즈니스 로직 수행
                    if(WebUtils.isRESTRequest(path)) {
                        // GET 요청일 경우
                        if(WebUtils.isGetRequest(headerLine[0])) {
                            path = webAdapter.resolveRequestUri(path, out);
                        }
                    }
                    // 설정한 path를 바탕으로 확장자에 맞게 contentType에 맞게 뷰 파일을 찾아 응답
                    extension = path.substring(path.lastIndexOf(".")+1);
                    contentType = WebUtils.getProperContentType(extension);
                    readAndResponseFromPath(out, dirPath+path, contentType);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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
