package handler;

import constant.FileExtensionType;
import constant.HttpMethod;
import constant.HttpStatus;
import constant.MimeType;
import db.Database;
import dto.HttpRequest;
import exception.InvalidHttpRequestException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String LOCATION = "Location";

    private final List<Map<String, Handler>> handlers;

    private HandlerManager(){
        // Http Method 종류별로 Handler를 저장하는 Map 생성
        handlers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            handlers.add(new HashMap<>());
        }

        // 각 HttpRequest를 처리하는 Handler 등록

        handlers.get(HttpMethod.POST.getHandlerMapIdx()).put("/user/create", (dos, httpRequest) -> {

            List<String> valueList = httpRequest.getHeader(CONTENT_TYPE).orElseThrow(
                    () -> new InvalidHttpRequestException("content type is empty")
            );

            MimeType contentType = null;
            for(String value : valueList){
                contentType = MimeType.findByTypeName(value);
                if(contentType != null)
                    break;
            }
            if(contentType == null)
                throw new InvalidHttpRequestException("content type is empty");

            Map<String, String> bodyParams = HttpRequestParser.parseRequestBody(httpRequest.getBody().orElseThrow(
                    () -> new InvalidHttpRequestException("request body is empty")),contentType);
            User user = new User(bodyParams);
            Database.addUser(user);

            response302Header(dos,"/login/index.html");
        });
    }

    private static class LazyHolder {
        private static final HandlerManager INSTANCE = new HandlerManager();
    }

    public static HandlerManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    // HttpRequest를 처리할 수 있는 Handler 반환
    public Handler getHandler(HttpRequest httpRequest){

        // 정적 파일 요청인 경우
        if(httpRequest.getExtensionType().isPresent()){
            return (dos, _httpRequest) -> {
                handleStaticResource(dos, _httpRequest.getPath().orElseThrow(
                        () -> new InvalidHttpRequestException("uri path is empty"))
                        , _httpRequest.getExtensionType().get());
            };
        }
        // API 요청인 경우
        else{
            HttpMethod httpMethod = httpRequest.getHttpMethod();
            String path = httpRequest.getPath().orElseThrow(
                    () -> new InvalidHttpRequestException("uri path is empty"));
            Handler handler = handlers.get(httpMethod.getHandlerMapIdx()).get(path);

            // HttpRequest를 처리할 handler가 없을 경우, 예외 발생
            if(handler == null)
                throw new InvalidHttpRequestException("handler not found");

            return handler;
        }
    }

    // 정적 파일 응답 메서드
    public void handleStaticResource(DataOutputStream dos, String filePath, String extensionType) throws IOException, IllegalArgumentException {
        logger.info("filePath: {}", filePath);
        byte[] body = readFile(filePath);

        if(body != null) {
            response200Header(dos, body.length, extensionType);
            responseBody(dos, body);
        }
        else{
            // url에 해당하는 파일이 없으면 404 error 응답
            response404Error(dos);
        }
    }

    // File Path에 해당하는 파일을 byte 배열로 반환
    public static byte[] readFile(String filePath) {
        if(filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        // 등록된 정적 파일 클래스패스를 통해 파일 읽기 (src/main/resources/static 디렉토리 안에 있는 파일)
        try (InputStream inputStream = HandlerManager.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filePath);
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    // Http Status 200 응답 메서드
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extensionType){
        try {
            FileExtensionType fileExtensionType = FileExtensionType.valueOf(extensionType.toUpperCase());

            dos.writeBytes(HTTP_VERSION + " " + HttpStatus.OK.getStatusCode() + " " + HttpStatus.OK.getMessage() + CRLF);
            dos.writeBytes(CONTENT_TYPE + ": " + fileExtensionType.getContentType() + ";"
                    + CHARSET_UTF8 + CRLF);
            dos.writeBytes(CONTENT_LENGTH + ": " + lengthOfBodyContent + CRLF+CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // Http Status 302 응답 메서드
    private static void response302Header(DataOutputStream dos, String redirectUrl){
        try {
            dos.writeBytes(HTTP_VERSION + " " + HttpStatus.FOUND.getStatusCode() + " "
                    + HttpStatus.FOUND.getMessage() + CRLF);
            dos.writeBytes(LOCATION + ": " + redirectUrl + CRLF + CRLF);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // Http Status 404 응답 메서드
    public void response404Error(DataOutputStream dos) throws IOException {
        try {
            String errorMessage = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            dos.writeBytes(HTTP_VERSION + " " + HttpStatus.NOT_FOUND.getStatusCode() + " "
                            + HttpStatus.NOT_FOUND.getMessage() + CRLF);
            dos.writeBytes(CONTENT_TYPE + ": " + FileExtensionType.HTML.getContentType() + CRLF);
            dos.writeBytes(CONTENT_LENGTH + ": " + errorMessage.length() + CRLF + CRLF);
            dos.writeBytes(errorMessage);
            dos.flush();
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }



}
