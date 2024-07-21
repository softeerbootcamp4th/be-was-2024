package handler;

import constant.*;
import exception.MethodNotAllowedException;
import handler.handlerimpl.LoginHandler;
import handler.handlerimpl.MainHandler;
import handler.handlerimpl.PostHandler;
import handler.handlerimpl.UserHandler;
import dto.HttpRequest;
import dto.HttpResponse;
import exception.InvalidHttpRequestException;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ErrorResponseBuilder;
import util.HttpRequestParser;

import java.io.*;
import java.util.*;

/**
 * HttpRequest를 처리할 수 있는 handler를 관리하는 클래스
 */
public class HandlerManager {
    private static final Logger logger = LoggerFactory.getLogger(HandlerManager.class);

    private final EnumMap<HttpMethod, Map<String, Handler>> handlers;


    private HandlerManager(){
        // Http Method 종류별로 Handler를 저장하는 EnumMap 생성
        handlers = new EnumMap<>(HttpMethod.class);
        for (HttpMethod method : HttpMethod.values()) {
            handlers.put(method, new HashMap<>());
        }

        // 각 HttpRequest를 처리하는 Handler 등록
        handlers.get(HttpMethod.GET).put("/", MainHandler.mainHandler);

        // 회원가입 handler
        handlers.get(HttpMethod.POST).put("/user/create", UserHandler.userCreateHanlder);

        // 로그인 처리 handler
        handlers.get(HttpMethod.POST).put("/user/login", LoginHandler.userLoginHandler);

        // 로그아웃 handler
        handlers.get(HttpMethod.POST).put("/logout", LoginHandler.logoutHandler);

        handlers.get(HttpMethod.GET).put("/user/list", UserHandler.userListHandler);

        handlers.get(HttpMethod.GET).put("/post/form", PostHandler.postFormHandler);

        handlers.get(HttpMethod.GET).put("/post/prev", PostHandler.postPrevHandler);

        handlers.get(HttpMethod.GET).put("/post/next", PostHandler.postNextHandler);

        handlers.get(HttpMethod.POST).put("/post", PostHandler.postHandler);
    }


    private static class LazyHolder {
        private static final HandlerManager INSTANCE = new HandlerManager();
    }

    public static HandlerManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * HttpRequest를 처리할 수 있는 Handler를 반환한다.
     *
     * @param httpRequest : HttpRequest의 정보를 갖고있는 객체
     * @return : 요청을 처리할 수 있는 Handler 객체
     */
    public Handler getHandler(HttpRequest httpRequest){

        // 정적 파일 요청인 경우
        if(httpRequest.getExtensionType().isPresent()){
            return (_httpRequest, _httpResponse) ->
                    handleStaticResource(httpRequest, _httpResponse);
        }
        // API 요청인 경우
        else{
            HttpMethod httpMethod = httpRequest.getHttpMethod();
            String path = httpRequest.getPath().orElseThrow(
                    () -> new InvalidHttpRequestException("uri path is empty"));
            Handler handler = handlers.get(httpMethod).get(path);


            if(handler == null){
                boolean isUrlExist = false;
                for(HttpMethod method : HttpMethod.values()){
                    for(String url : handlers.get(method).keySet()){
                        if (url.equals(path)) {
                            isUrlExist = true;
                            break;
                        }
                    }
                }

                // url은 존재하지만 HttpMethod가 잘못된 경우
                if(isUrlExist)
                    throw new MethodNotAllowedException("method not allowed");
                // url이 잘못된 경우
                else
                    throw new InvalidHttpRequestException("handler not found");
            }

            return handler;
        }
    }

    // HttpRequest의 content type에 따른 HttpRequest body 파싱 및 반환

    /**
     * application/x-www-form-urlencoded 형식의 데이터를 파싱하여 반환한다.
     *
     * @param httpRequest : HttpRequest의 정보를 갖고있는 객체
     * @return : application/x-www-form-urlencoded 형식의 데이터가 저장된 map
     */
    public static Map<String, String> getBodyParams(HttpRequest httpRequest){

        List<String> valueList = httpRequest.getHeader(HttpResponseAttribute.CONTENT_TYPE.getValue()).orElseThrow(
                () -> new InvalidHttpRequestException("content type is empty")
        );

        MimeType contentType = null;
        for(String value : valueList){
            contentType = MimeType.of(value);
            if(contentType != null)
                break;
        }
        if(contentType != MimeType.APPLICATION_FORM_URLENCODED)
            throw new InvalidHttpRequestException("content type is invalid");

        return HttpRequestParser.parseUrlEncodedFormData(httpRequest);

    }

    /**
     * 정적 파일 데이터를 httpResponse 객체에 저장한다.
     *
     * @param httpRequest : HttpRequest의 정보를 갖고있는 객체
     * @param httpResponse : 응답 데이터를 저장하는 HttpResponse 객체
     */
    public static void handleStaticResource(HttpRequest httpRequest, HttpResponse httpResponse) {

        byte[] body = readStaticFile(httpRequest.getPath().orElseThrow(
                () -> new InvalidHttpRequestException("invalid path")));

        String extensionType = httpRequest.getExtensionType().get().toUpperCase();

        if(body.length!=0) {
            // 정적 파일 응답 생성
            httpResponse.setHttpStatus(HttpStatus.OK);
            httpResponse.addHeader(HttpResponseAttribute.CONTENT_TYPE.getValue(), FileExtensionType.valueOf(extensionType).getContentType());
            httpResponse.addHeader(HttpResponseAttribute.CONTENT_TYPE.getValue(), HttpResponseAttribute.CHARSET_UTF8.getValue());
            httpResponse.addHeader(HttpResponseAttribute.CONTENT_LENGTH.getValue(), String.valueOf(body.length));
            httpResponse.setBody(body);
        }
        else{
            // url에 해당하는 파일이 없으면 404 error 응답
            ErrorResponseBuilder.buildErrorResponse(HttpStatus.NOT_FOUND, httpResponse);
        }
    }

    /**
     * File path에 해당하는 파일을 읽어 byte 배열로 반환한다.
     *
     * @param filePath : 파일의 path
     * @return : 파일 데이터를 byte[] 형태로 반환
     */
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
            return new byte[0];
        }
    }

}
