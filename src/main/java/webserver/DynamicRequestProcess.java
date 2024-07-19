package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import data.MultipartFile;
import db.PostDatabase;
import db.UserDatabase;
import db.Session;
import exception.BadMethodException;
import handler.ViewHandler;
import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MultiPartParser;
import util.MultiPartUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DynamicRequestProcess {
    private static final Logger log = LoggerFactory.getLogger(DynamicRequestProcess.class);

    public static HttpResponseMessage registration(HttpRequestMessage httpRequestMessage){
        if (!httpRequestMessage.getMethod().equals("POST")) throw new BadMethodException("Method not supported");
        Map<String,String> map = new HashMap<>();
        String body = new String(httpRequestMessage.getBody());
        String[] bodySplit = body.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            map.put(keyValue[0],keyValue[1]);
        }
        UserDatabase.addUser(new User(map.get("userId"), map.get("password"), URLDecoder.decode(map.get("name"),StandardCharsets.UTF_8), map.get("email")));
        map.clear();
        map.put("Location","/index.html");
        return new HttpResponseMessage("303",map,null);
    }
    public static HttpResponseMessage login(HttpRequestMessage httpRequestMessage){
        if (!httpRequestMessage.getMethod().equals("POST")) throw new BadMethodException("Method not supported");
        Map<String,String> map = new HashMap<>();
        String requestBody = new String(httpRequestMessage.getBody());
        String[] bodySplit = requestBody.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            map.put(keyValue[0],keyValue[1]);
        }
        User user = UserDatabase.findUserById(map.get("userId"));
        if (user == null || !user.getPassword().equals(map.get("password"))){
            map.clear();
            map.put("Location","/login");
            return new HttpResponseMessage("303", map,null);
        }
        map.clear();
        String sessionId = UUID.randomUUID().toString();
        map.put("Set-Cookie","SID="+sessionId + "; Path=/");
        map.put("Location","/index.html");
        Session.addNewSession(sessionId,user);
        return new HttpResponseMessage("303",map,null);
    }

    public static HttpResponseMessage home(HttpRequestMessage httpRequestMessage) throws IOException {
        if (!httpRequestMessage.getMethod().equals("GET")) throw new BadMethodException("Method not supported");
        Map<String,String> param = new HashMap<>();
        String page = httpRequestMessage.getQueryParam().get("page");
        if (page == null){
            Long minimumPostId = PostDatabase.getMinimumPostId();
            if (minimumPostId == 0){
                param.put("writer","아직 게시글이 없어요");
                param.put("content", "아직 게시글이 없어요");
            }
            else {
                page = String.valueOf(minimumPostId);
                param.put("Location", "/?page=" + page);
                return new HttpResponseMessage("303", param, null);
            }
        }
        else{
            Post post = PostDatabase.getPost(Long.parseLong(page));
            if (post == null){
                param.put("Location", "/?page=" + (Long.parseLong(page) - 1));
                return new HttpResponseMessage("303", param, null);
            }
            param.put("writer", post.getAuthorName());
            param.put("content", post.getContent());
            String image = post.getImage();
            if (image == null) param.put("image","");
            else param.put("image", "src=\"/post-images/" + image + "\"");
        }
        Map<String, String> cookies = httpRequestMessage.getCookies();
        HashMap<String, String> headers = new HashMap<>();
        String sid = cookies.get("SID");
        String unLoginedHomeView = null;
        if (sid == null){
            unLoginedHomeView = ViewHandler.viewParamProcess("src/main/resources/static/index.html",param);
            return new HttpResponseMessage("200",headers,unLoginedHomeView.getBytes());
        }
        User user = Session.getUser(sid);
        if (user == null) {
            unLoginedHomeView = ViewHandler.viewParamProcess("src/main/resources/static/index.html",param);
            return new HttpResponseMessage("200",headers,unLoginedHomeView.getBytes());
        }
        param.put("userName", user.getName());
        String loginedHomeView = ViewHandler.viewParamProcess("src/main/resources/static/main/index.html",param);
        return new HttpResponseMessage("200",headers,loginedHomeView.getBytes());
    }

    public static HttpResponseMessage userList(HttpRequestMessage httpRequestMessage) throws IOException {
        if (!httpRequestMessage.getMethod().equals("GET")) throw new BadMethodException("Method not supported");
        Map<String, String> cookies = httpRequestMessage.getCookies();
        Map<String,String> headers = new HashMap<>();
        List<String> list = UserDatabase.findAll();
        String sid = cookies.get("SID");
        if (sid == null) return new HttpResponseMessage("303",headers,null);
        User user = Session.getUser(sid);
        if (user == null) return new HttpResponseMessage("303",headers,null);
        String html = ViewHandler.viewListProcess("src/main/resources/static/user/list.html", list);
        return new HttpResponseMessage("200",headers,html.getBytes());
    }

    public static HttpResponseMessage logout(HttpRequestMessage httpRequestMessage) throws IOException {
        if (!httpRequestMessage.getMethod().equals("POST")) throw new BadMethodException("Method not supported");
        Map<String,String> headers = new HashMap<>();
        headers.put("Location","/index.html");
        headers.put("Set-Cookie","SID=null;MAX-AGE=0");
        Session.removeSession(httpRequestMessage.getCookies().get("SID"));
        return new HttpResponseMessage("303",headers,null);
    }

    public static HttpResponseMessage article(HttpRequestMessage httpRequestMessage) throws IOException {
        if (!httpRequestMessage.getMethod().equals("GET")) throw new BadMethodException("Method not supported");
        Map<String, String> cookies = httpRequestMessage.getCookies();
        Map<String,String> headers = new HashMap<>();
        String sid = cookies.get("SID");
        try {
            if (sid == null || UserDatabase.findUserById(Session.getUser(sid).getUserId()) == null) {
                headers.put("Location", "/login");
                return new HttpResponseMessage("303", headers, null);
            }
        } catch (NullPointerException e) {
            headers.put("Location", "/login");
            return new HttpResponseMessage("303", headers, null);
        }
        return UriMapper.staticRequestProcess("src/main/resources/static/article/index.html");
    }

    public static HttpResponseMessage postArticle(HttpRequestMessage httpRequestMessage) throws IOException {
        if (!httpRequestMessage.getMethod().equals("POST")) throw new BadMethodException("Method not supported");
        Map<String, String> cookies = httpRequestMessage.getCookies();
        Map<String,String> headers = new HashMap<>();
        String sid = cookies.get("SID");
        User user;
        if (sid == null || (user = UserDatabase.findUserById(Session.getUser(sid).getUserId())) == null) {
            headers.put("Location","/login");
            return new HttpResponseMessage("303",headers,null);
        }
        byte[] body = httpRequestMessage.getBody();
        byte[] boundary = MultiPartUtils.getBoundary(httpRequestMessage.getHeaders());
        List<MultipartFile> multipartFiles = MultiPartParser.parse(body,boundary);
        Post post = MultiPartUtils.processMultiPart(user.getId(), user.getName(), multipartFiles);
        PostDatabase.addPost(post);
        headers.put("Location","/");
        return new HttpResponseMessage("303",headers,null);
    }
}
