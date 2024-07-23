package handler.handlerimpl;

import cookie.RedirectCookie;
import dto.PostDto;
import handler.Handler;
import model.MyTagDomain;
import model.Post;
import model.User;
import repository.PostRepository;
import repository.UserRepository;
import session.Session;
import util.DynamicFileBuilder;
import util.HttpRequestParser;
import dto.multipart.MultiPartData;

import java.sql.Connection;
import java.util.*;
import static handler.HandlerManager.handleStaticResource;
import static repository.DatabaseConnection.getConnection;

/**
 * 게시글 관련 작업을 처리하는 클래스
 */
public class PostHandler {

    // 게시글 저장 handler
    public static Handler postFormHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        // 쿠키에 있는 sessionId가 유효한지 검사
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){

                httpRequest.setPath("/post/form.html");
                httpRequest.setExtensionType("html");
                handleStaticResource(httpRequest, httpResponse);
                return;
            }
        }
        RedirectCookie cookie = new RedirectCookie("/post/form");
        httpResponse.setCookie(cookie);

        httpRequest.setPath("/login/index.html");
        httpRequest.setExtensionType("html");
        handleStaticResource(httpRequest, httpResponse);
    };

    // 이전 게시글을 보여주는 handler
    public static Handler postPrevHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, List<MyTagDomain>> model = new HashMap<>();

        // 로그인 상태일 때, model에 user객체 저장
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){

                User user = UserRepository.findByUserId(userId, connection);
                List<MyTagDomain> userList = new ArrayList<>();
                userList.add(user);
                model.put("login", userList);
            }
        }
        // HttpRequest에서 경로 변수(post의 id값) 가져오기
        int pathVariable = httpRequest.getPathVariable().get();
        // 현재 게시글의 이전에 해당하는 게시글을 가져오기
        Optional<Post> optionalPost= PostRepository.findPreviousPost(pathVariable, connection);

        // 게시글이 존재하면 해당 게시글을 보여주기
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            String userId = UserRepository.findById(post.getUserId(), connection).getUserId();
            List<MyTagDomain> postList = new ArrayList<>();
            postList.add(new PostDto(post, userId));
            model.put("post", postList);

            DynamicFileBuilder.setHttpResponse(httpResponse, "/post/post.html", model);
        }
        // 게시글이 존재하지 않는다면 기본 화면을 보여주기
        else{
            DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
        }

    };

    // 다음 게시글을 보여주는 handler
    public static Handler postNextHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, List<MyTagDomain>> model = new HashMap<>();
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){

                User user = UserRepository.findByUserId(userId, connection);
                List<User> userList = new ArrayList<>();
                userList.add(user);
                model.put("login", new ArrayList<>(userList));
            }
        }
        // HttpRequest에서 경로 변수(post의 id값) 가져오기
        int pathVariable = httpRequest.getPathVariable().get();
        // 현재 게시글의 다음에 해당하는 게시글을 가져오기
        Optional<Post> optionalPost= PostRepository.findNextPost(pathVariable, connection);

        // 게시글이 존재하면 해당 게시글을 보여주기
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            String userId = UserRepository.findById(post.getUserId(), connection).getUserId();
            List<MyTagDomain> postList = new ArrayList<>();
            postList.add(new PostDto(post, userId));
            model.put("post", postList);

            DynamicFileBuilder.setHttpResponse(httpResponse, "/post/post.html", model);
        }
        // 게시글이 존재하지 않는다면 기본 화면을 보여주기
        else{
            DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
        }

    };

    // 게시글을 저장하는 handler
    public static Handler postHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        // 쿠키에 있는 sessionId가 유효한지 검사
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){

                Map<String, MultiPartData> postData = HttpRequestParser.parseMultipartFormData(httpRequest);

                PostRepository.addPost(new Post(postData, UserRepository.findByUserId(userId, connection).getId())
                        , connection);
                httpResponse.setRedirect("/");
            }
        }
    };
}
