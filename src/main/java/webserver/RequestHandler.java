package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import ApiProcess.ApiProcess;

import enums.HttpResult;
import enums.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MimeTypeMapper;
import utils.PathUtils;
import utils.RequestParser;
import utils.ResourcesLoader;

public class RequestHandler implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
            Response response = new Response();

            /**
             * 경로에 따른 로직 처리
             * ApiProcess 인터페이스를 만들어 로직 처리를 추상화
             * ApiProcessManager가 해당 하는 구체화 클래스를 주입 합니다.
             * RequestHandler는 로직 처리를 위한 구체화 클래스를 알지 못해도 된다.
             */
            ApiProcessManager apiProcessManager = new ApiProcessManager();
            ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath(), request.getMethod());
            String fileNameWithoutExt = apiProcess.process(request, response);

            // 리다이렉트 시
            if(isRedirect(response)) {
               response.sendRedirect(dos, fileNameWithoutExt);
               return null;
            }

            // 확장자를 넣어주는 메서드 사용
            String fileName = PathUtils.filePathResolver(fileNameWithoutExt);

            logger.debug("fileName = {}", fileName);

            // 파일의 바이너리 데이터 읽기
            byte[] body = ResourcesLoader.getFile(fileName);

            // mimeType 변환
            MimeType mimeType = PathUtils.extToMimetype(fileName);
            response.setContentType(mimeType);

            // 클라이언트에게 파일 반환
            response.send(dos, body);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private static boolean isRedirect(Response response) {
        return response.getHttpCode() != null
                && response.getHttpCode().getHttpResult().equals(HttpResult.REDIRECT);
    }
}
