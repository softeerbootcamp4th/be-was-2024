package handler;

import constant.FileExtensionType;
import constant.HttpMethod;
import db.Database;
import dto.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private Map<String, Handler>[] handlers;

    private HandlerManager(){
        // Http Method 종류별로 Handler를 저장하는 Map 생성
        handlers = new HashMap[5];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new HashMap<>();
        }

        // 각 HttpRequest를 처리하는 Handler 등록

        handlers[HttpMethod.GET.getHandlerMapIdx()].put("/user/create", (dos, queryParams) -> {
            User user = new User(queryParams);
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
            return (dos, queryParams) -> {
                handleStaticResource(dos, httpRequest.getPath().orElseThrow(
                        () -> new IllegalArgumentException("An invalid Http request was received"))
                        , httpRequest.getExtensionType().get());
            };
        }
        // API 요청인 경우
        else{
            HttpMethod httpMethod = httpRequest.getHttpMethod();
            String path = httpRequest.getPath().orElseThrow(
                    () -> new IllegalArgumentException("An invalid Http request was received"));
            return handlers[httpMethod.getHandlerMapIdx()].get(path);
        }
    }

    // 정적 파일 응답 메서드
    public void handleStaticResource(DataOutputStream dos, String filePath, String extensionType) throws IOException, IllegalArgumentException {
        logger.info("filePath: {}", filePath);
        byte[] body = readFile("src/main/resources/static"+filePath);

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
    private byte[] readFile(String filePath) {
        File file = new File(filePath);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] temp = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(temp)) != -1) {
                buffer.write(temp, 0, bytesRead);
            }

            return buffer.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    // Http Status 200 응답 메서드
    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extensionType){
        try {
            FileExtensionType fileExtensionType = FileExtensionType.valueOf(extensionType.toUpperCase());

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileExtensionType.getContentType() + ";"
                    + "charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // Http Status 302 응답 메서드
    private static void response302Header(DataOutputStream dos, String redirectUrl){
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // Http Status 404 응답 메서드
    public void response404Error(DataOutputStream dos) throws IOException {
        try {
            String errorMessage = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html\r\n");
            dos.writeBytes("Content-Length: " + errorMessage.length() + "\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes(errorMessage);
            dos.flush();
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    // Response Body 생성 및 HttpResponse를 client에게 전송
    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
