package webserver;

import common.*;
import db.UserH2Database;
import exception.CannotResolveRequestException;
import facade.ArticleFacade;
import facade.SessionFacade;
import facade.UserFacade;
import file.ViewFile;
import model.Session;
import model.User;
import web.HttpRequest;
import web.HttpResponse;
import web.RestUri;
import web.ViewPath;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 요청 헤더에서 필요한 정보를 추출하는 클래스
 */
public class WebAdapter {
    static final String SESSION_ID = "SID";

    public static void resolveRequest(HttpRequest request, OutputStream out) throws IOException {
        if(request.isGetRequest()) {
            resolveGetRequest(request, out);
        } else if(request.isPostRequest()) {
            resolvePostRequest(request, out);
        }
        else throw new CannotResolveRequestException("Cannot Resolve Request Method");
    }

    /**
     * POST 요청을 받아서 비즈니스 로직 처리
     * SIGN_UP - 유저 생성, DB 저장
     * SIGN_IN - ID, PWD 검증후 세션 포함하여 홈화면 리다이렉트
     * LOGOUT - 세션 저장소 swap 이후 홈화면 리다이렉트
     * ARTICLE - multipart-form-data 파싱후 이미지 저장, 데이터베이스에 정보 저장
     */
    private static void resolvePostRequest(HttpRequest request, OutputStream out) throws IOException {
        // POST registration
        if(request.getPath().equals(RestUri.SIGN_UP.getUri())) {
            // body의 유저 정보 파싱
            Map<String, String> map = StringUtils.parseBodyInForm(request.getBody());
            // 유저 생성
            UserFacade.createUser(new User(map.get("userId"), map.get("password"), map.get("name"), ""));

            HttpResponse response = ResponseUtils.redirectToView(ViewPath.DEFAULT);
            response.writeInBytes(out);

        } else if(request.getPath().equals(RestUri.SIGN_IN.getUri())) {
            Map<String, String> map = StringUtils.parseBodyInForm(request.getBody()); // userId, password
            HttpResponse response;

            if(UserFacade.isUserExist(map)) { // id, pw 일치하다면
                Session newSession = SessionFacade.createSession(map.get("userId"));

                Map<String, String> hashMap = new ConcurrentHashMap<>();
                hashMap.put(SESSION_ID, newSession.getId());

                response = ResponseUtils.redirectToViewWithCookie(hashMap);

            } else { // id, pwd 불일치
                response = ResponseUtils.redirectToView(ViewPath.LOGIN);
            }

            response.writeInBytes(out);

        } else if(request.getPath().equals(RestUri.LOGOUT.getUri())) {
            // 세션 저장소의 정보 삭제
            SessionFacade.invalidateAndRemoveSession(request);

            HttpResponse response = ResponseUtils.redirectToView(ViewPath.DEFAULT);
            response.writeInBytes(out);
        } else if(request.getPath().equals(RestUri.ARTICLE.getUri())) {
            // 게시글 데이터 및 DB 저장
            ArticleFacade.saveArticleData(request);

            // 화면 리다이렉트
            HttpResponse response = ResponseUtils.redirectToView(ViewPath.DEFAULT);
            response.writeInBytes(out);
        } else {
            resolveGetRequest(ViewPath.NOT_FOUND.getRequestUri());
        }
    }

    /**
     * 비즈니스 로직 처리가 필요하다면 처리한 후, 뷰 응답
     */
    private static void resolveGetRequest(HttpRequest request, OutputStream out) throws IOException {
        // GET으로 회원가입 요청시 400 응답
        if(request.getPathWithoutQueryParam().equals(RestUri.SIGN_UP.getUri())) {
            HttpResponse response = ResponseUtils.responseBadRequest();
            response.writeInBytes(out);
            return;
        }
        // 유저 리스트 찾아서 json으로 반환
        else if(request.getPathWithoutQueryParam().equals(RestUri.USER_LIST.getUri())) {
            HttpResponse response;
            // 인증된 요청일경우 표시
            if(SessionFacade.isAuthenticatedRequest(request)) {
                Collection<User> users = UserH2Database.findAll();
                String jsonUser = JsonBuilder.buildJsonResponse(users);

                response = ResponseUtils.responseSuccessWithJson(jsonUser.length(), jsonUser.getBytes());
            } else {
                response = ResponseUtils.redirectToView(ViewPath.DEFAULT);
            }
            response.writeInBytes(out);
            return;
        }
        // 데이터베이스 초기화
        else if(request.getPathWithoutQueryParam().equals(RestUri.DATABASE_INIT.getUri())) {
            UserH2Database.removeAll();

            HttpResponse response = ResponseUtils.responseSuccess();
            response.writeInBytes(out);
            return;
        }
        // 글쓰기 창 이동
        else if(request.getPath().equals(RestUri.ARTICLE.getUri())) {
            HttpResponse response;
            if(SessionFacade.isAuthenticatedRequest(request)) {
                response = ResponseUtils.redirectToView(ViewPath.ARTICLE);
            } else {
                response = ResponseUtils.redirectToView(ViewPath.LOGIN);
            }
            response.writeInBytes(out);
            return;
        }

        // 별도 GET 처리 로직이 없는경우 뷰를 찾아 반환
        String filePath = resolveGetRequest(request.getPath());
        ViewFile viewFile = new ViewFile(filePath, FileUtils.getExtensionFromPath(filePath));
        ViewResolver.readAndResponseFromPath(request, out, FileUtils.getStaticFilePath(viewFile.getPath()), WebUtils.getProperContentType(viewFile.getExtension()));
    }

    /**
     * GET 요청에 적절한 뷰를 응답해준다
     */
    private static String resolveGetRequest(String restUri) {
        return ViewPath.findByRequestUri(restUri).getFilePath();
    }
}
