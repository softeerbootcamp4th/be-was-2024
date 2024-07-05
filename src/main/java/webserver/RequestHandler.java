package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import ApiProcess.ApiProcess;

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

            /**
             * 경로에 따른 로직 처리
             * ApiProcess 인터페이스를 만들어 로직 처리를 추상화
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

            // 렌더링할 파일의 실제 위치
            String filePath = filePathResolver(fileName);

            // 파일의 raw 파일 읽기
            byte[] body = fileReadToBinary(filePath);

            // mimeType 변환
            MimeType mimeType = extToMimetype(filePath);
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
          return DEFALUT_PATH + fileName;
       }
       return DEFALUT_PATH + fileName + ".html";
    }

    private MimeType extToMimetype(String filePath) {
        // 파일 확장자를 mimeType으로 바꾸는 과정
        String fileExt = fileParser.fileExtExtract(filePath);
        MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
        return mimeTypeMapper.getMimeType(fileExt);
    }

    private byte[] fileReadToBinary(String filePath) throws IOException {
       // binary로 파일읽기
        byte[] body;
        try(
                FileInputStream fis = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ) {
            bis.transferTo(bos);
            body = bos.toByteArray();
        }
        return body;
    }
}
