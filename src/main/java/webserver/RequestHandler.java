package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import ApiProcess.ApiProcess;

import enums.HttpCode;
import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HttpRequestParser;
import utils.MimeTypeMapper;
import utils.PathParser;

public class RequestHandler implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final PathParser fileParser = new PathParser();
    public static final String DEFALUT_PATH = "src/main/resources/static";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public Void call() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            DataOutputStream dos = new DataOutputStream(out);

            // request, response 객체 생성
            HttpRequestParser httpRequestParser = new HttpRequestParser();
            Request request = httpRequestParser.getRequest(in);
            Response response = new Response();

            // 경로에 따른 로직 처리 매핑
            ApiProcessManager apiProcessManager = new ApiProcessManager();
            ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath());
            String viewName = apiProcess.process(request, response);

            // 리다이렉트 시
            if(response.getHttpCode() != null) {
               response.sendRedirect(dos, viewName);
               return null;
            }

            // 렌더링할 파일 위치
            String htmlPath = DEFALUT_PATH + viewName;

            // 파일 read
            byte[] body;
            try(
                    FileInputStream fis = new FileInputStream(htmlPath);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ) {
                bis.transferTo(bos);
                body = bos.toByteArray();
            }

            // 파일 확장자를 mimeType으로 바꾸는 과정
            String fileExt = fileParser.fileExtExtract(htmlPath);
            MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
            MimeType mimeType = mimeTypeMapper.getMimeType(fileExt);

            // html render
            response.setContentType(mimeType);
            response.send(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private void responseHeader(DataOutputStream dos, Response response) {
        HttpCode httpCode = response.getHttpCode() == null ? HttpCode.OK : response.getHttpCode();
        try {
            dos.writeBytes("HTTP/1.1 " + httpCode.getCode() + " " + httpCode.getMessage() + "\r\n");
            dos.writeBytes("Content-Type: " + response.getContentType() + "\r\n");
            dos.writeBytes("Content-Length: " + response.getContentLength() + "\r\n");
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
