package handler;

import constant.FileExtensionType;
import constant.HttpMethod;
import constant.HttpStatus;
import constant.MimeType;
import cookie.Cookie;
import cookie.RedirectCookie;
import cookie.SessionCookie;
import db.Database;
import dto.HttpRequest;
import dto.HttpResponse;
import exception.InvalidHttpRequestException;
import exception.ResourceNotFoundException;
import model.MyTagDomain;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.Session;
import util.DynamicFileBuilder;
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
        handlers.get(HttpMethod.GET).put("/", (httpRequest, httpResponse) -> {

            Map<String, List<MyTagDomain>> model = new HashMap<>();

            // 쿠키에 있는 sessionId가 유효한지 검사
            if(httpRequest.getSessionId().isPresent()){
                String sessionId = httpRequest.getSessionId().get();
                String userId = Session.getUserId(sessionId);
                if(userId != null && Database.userExists(userId)){
                    User user = Database.findUserById(userId);
                    List<User> userList = new ArrayList<>();
                    userList.add(user);
                    model.put("login", new ArrayList<>(userList));
                    // 동적 index.html 반환
                    DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
                    return;
                }
            }

            // 쿠키에 세션 정보가 없거나 유효하지 않으면 빈 model 전달
            DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
        });

        // 회원가입 handler
        handlers.get(HttpMethod.POST).put("/user/create", (httpRequest, httpResponse) -> {

            Map<String, String> bodyParams = getBodyParams(httpRequest);

            // User를 DB에 저장
            User user = new User(bodyParams);
            Database.addUser(user);

            // 302 응답 생성
            httpResponse.setRedirect("/");
        });

        // 로그인 처리 handler
        handlers.get(HttpMethod.POST).put("/user/login", (httpRequest, httpResponse) -> {

            Map<String, String> bodyParams = getBodyParams(httpRequest);

            String userId = bodyParams.get("userId");
            String password = bodyParams.get("password");
            if(userId == null || password == null){
                httpResponse.setRedirect("/login/login_failed.html");
                return;
            }

            if(Database.userExists(bodyParams.get("userId"))){

                User user = Database.findUserById(userId);

                // 로그인 성공 시, /index.html로 redirect
                if(user.getPassword().equals(password)){
                    String sessionId = Session.createSession(userId);
                    httpResponse.setCookie(new SessionCookie(sessionId));

                    // 로그인을 하기 전에 유저 리스트 버튼을 눌렀다면
                    // /user/list로 리다이렉트 하고, redirect 쿠키를 삭제
                    if(httpRequest.getRedirectUrl().isPresent() &&
                        httpRequest.getRedirectUrl().get().equals("/user/list")){

                        RedirectCookie cookie = new RedirectCookie("/user/list");
                        cookie.setMaxAge(0);
                        httpResponse.setCookie(cookie);
                        httpResponse.setRedirect("/user/list");
                    }
                    else
                        httpResponse.setRedirect("/");
                    return;
                }
            }

            // 로그인 실패 시, /login/failed.html로 redirect
            httpResponse.setRedirect("/login/login_failed.html");

        });

        // 로그아웃 handler
        handlers.get(HttpMethod.POST).put("/logout", (httpRequest, httpResponse) -> {

            if(httpRequest.getSessionId().isPresent()){
                String sessionId = httpRequest.getSessionId().get();
                Session.deleteSession(sessionId);

                // 세션 쿠키 삭제
                SessionCookie sessionCookie = new SessionCookie(sessionId);
                sessionCookie.setMaxAge(0);
                httpResponse.setCookie(sessionCookie);
            }

            httpResponse.setRedirect("/");

        });

        handlers.get(HttpMethod.GET).put("/user/list", (httpRequest, httpResponse) -> {
            Map<String, List<MyTagDomain>> model = new HashMap<>();

            // 쿠키에 있는 sessionId가 유효한지 검사
            if(httpRequest.getSessionId().isPresent()){
                String sessionId = httpRequest.getSessionId().get();
                String userId = Session.getUserId(sessionId);
                if(userId != null && Database.userExists(userId)){
                    List<User> users = Database.findAllByList();
                    List<MyTagDomain> userList = new ArrayList<>(users);
                    model.put("userlist", userList);
                    // 동적 index.html 반환
                    DynamicFileBuilder.setHttpResponse(httpResponse, "/user/list.html", model);
                    return;
                }
            }

            // 쿠키에 세션 정보가 없거나 유효하지 않으면 로그인 페이지로 이동
            // 로그인 성공 시, /user/list로 redirect 할 수 있도록 redirect cookie 설정
            RedirectCookie cookie = new RedirectCookie("/user/list");
            httpResponse.setCookie(cookie);
            httpResponse.setRedirect("/login/index.html");
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
    public void handleStaticResource(HttpRequest httpRequest, HttpResponse httpResponse) throws IllegalArgumentException {

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
            httpResponse.setErrorResponse(HttpStatus.NOT_FOUND);
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
