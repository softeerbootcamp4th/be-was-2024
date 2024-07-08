package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import ApiProcess.ApiProcess;

import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MimeTypeMapper;
import utils.PathParser;
import utils.RequestParser;
import utils.ResourcesLoader;

public class RequestHandler implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final PathParser pathParser = PathParser.getInstance();
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
            RequestParser requestParser = RequestParser.getRequestParser();
            Request request = requestParser.getRequest(in);
            logger.debug("body test = {}", request.getBody());
            Response response = new Response();

            /**
             * 경로에 따른 로직 처리
             * ApiProcess 인터페이스를 만들어 로직 처리를 추상화
             * ApiProcessManager가 해당 하는 구체화 클래스를 주입 합니다.
             * RequestHandler는 로직 처리를 위한 구체화 클래스를 알지 못해도 된다.
             */
            ApiProcessManager apiProcessManager = new ApiProcessManager();
            ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath());
            String fileName = apiProcess.process(request, response);

            // 리다이렉트 시
            if(response.getHttpCode() != null) {
               response.sendRedirect(dos, fileName);
               return null;
            }

            fileName = filePathResolver(fileName);

            // 파일의 바이너리 데이터 읽기
            byte[] body = fileReadToBinary(fileName);

            // mimeType 변환
            MimeType mimeType = extToMimetype(fileName);
            response.setContentType(mimeType);

            // 클라이언트에게 파일 반환
            response.send(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private String filePathResolver(String fileName) {
       if(fileName.contains(".")) {
          return fileName;
       }
       return fileName + ".html";
    }

    private MimeType extToMimetype(String filePath) {
        // 파일 확장자를 mimeType으로 바꾸는 과정
        String fileExt = pathParser.fileExtExtract(filePath);
        MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
        return mimeTypeMapper.getMimeType(fileExt);
    }

    private byte[] fileReadToBinary(String filePath) {
        if(filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        try (InputStream inputStream = RequestHandler.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filePath);
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
