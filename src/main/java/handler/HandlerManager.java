package handler;

import constant.FileExtensionType;
import constant.HttpMethod;
import constant.HttpStatus;
import constant.MimeType;
import db.Database;
import dto.HttpRequest;
import dto.HttpResponse;
import exception.InvalidHttpRequestException;
import exception.ResourceNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

import java.io.*;
import java.util.*;

public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CHARSET_UTF8 = "utf-8";
    private static final String ERROR_MESSAGE_404 =
            "<html>" +
            "<head><title>404 Not Found</title></head>" +
            "<body><h1>404 Not Found</h1></body>" +
            "</html>";

    private final EnumMap<HttpMethod, Map<String, Handler>> handlers;

    private HandlerManager(){
        // Http Method 종류별로 Handler를 저장하는 EnumMap 생성
        handlers = new EnumMap<>(HttpMethod.class);
        for (HttpMethod method : HttpMethod.values()) {
            handlers.put(method, new HashMap<>());
        }

        // 각 HttpRequest를 처리하는 Handler 등록

        handlers.get(HttpMethod.POST).put("/user/create", (httpRequest, httpResponse) -> {

            Map<String, String> bodyParams = getBodyParams(httpRequest);

            // User를 DB에 저장
            User user = new User(bodyParams);
            Database.addUser(user);

            // 302 응답 생성
            httpResponse.setRedirect("/index.html");
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
            return this::handleStaticResource;
        }
        // API 요청인 경우
        else{
            HttpMethod httpMethod = httpRequest.getHttpMethod();
            String path = httpRequest.getPath().orElseThrow(
                    () -> new InvalidHttpRequestException("uri path is empty"));
            Handler handler = handlers.get(httpMethod).get(path);

            // HttpRequest를 처리할 handler가 없을 경우, 예외 발생
            if(handler == null)
                throw new InvalidHttpRequestException("handler not found");

            return handler;
        }
    }

    // HttpRequest의 content type에 따른 HttpRequest body 파싱 및 반환
    private Map<String, String> getBodyParams(HttpRequest httpRequest){

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

        return HttpRequestParser.parseRequestBody(httpRequest.getBody().orElseThrow(
                () -> new InvalidHttpRequestException("request body is empty")),contentType);

    }

    // 정적 파일 응답 메서드
    public void handleStaticResource(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, IllegalArgumentException {

        byte[] body = readStaticFile(httpRequest.getPath().orElseThrow(
                () -> new InvalidHttpRequestException("invalid path")));

        String extensionType = httpRequest.getExtensionType().get().toUpperCase();

        if(body != null) {
            // 정적 파일 응답 생성
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.valueOf(extensionType).getContentType());
            httpResponse.addHeader(CONTENT_TYPE, CHARSET_UTF8);
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
            httpResponse.setBody(body);
        }
        else{
            // url에 해당하는 파일이 없으면 404 error 응답
            httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.valueOf(extensionType).getContentType());
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(ERROR_MESSAGE_404.length()));
            httpResponse.setBody(ERROR_MESSAGE_404.getBytes(CHARSET_UTF8));
        }
    }

    // File Path에 해당하는 파일을 byte 배열로 반환
    public static byte[] readStaticFile(String filePath) {
        if(filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }

        logger.debug("filePath: {}", filePath);
        // 등록된 정적 파일 클래스패스를 통해 파일 읽기 (src/main/resources/static 디렉토리 안에 있는 파일)
        try (InputStream inputStream = HandlerManager.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new ResourceNotFoundException("Resource not found: " + filePath);
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }







}
