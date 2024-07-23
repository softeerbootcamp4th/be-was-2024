package handler;

import db.Database;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileDetection;
import util.RequestObject;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;


/**
 * Get메소드로 들어온 요청을 다뤄주는 클래스
 */
public class GetHandler {
    private GetHandler() {}

    private static final Logger logger = LoggerFactory.getLogger(GetHandler.class);
    private static class LazyHolder{
        private static final GetHandler INSTANCE = new GetHandler();
    }

    /**
     * LazyHolder형식으로 싱글톤 생성 위한 클래스
     */
    public static GetHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Get 메소드로 들어온 요청의 경로값을 파싱해서 다시 해당하는 메소드를 호출하는 클래스
     * @param dos DataoutputStream
     * @param requestObject 들어온 요청을담은 객체
     */
    public void handleGetRequest(DataOutputStream dos, RequestObject requestObject) {
        String path = requestObject.getPath();
        try {
            switch(path) {
                case "/user/info" : handleUserInfoRequest(dos,requestObject);
                                    break;
                case "/write"   : case "/user/list" : checkCookies(dos,requestObject ,path);
                                    break;
                case "/board/list" : sendBoardList(dos);
                                     break;
                default :  path= FileDetection.getPath(FileDetection.fixedPath+path);
                                staticFileHandler(dos,path);
                                break;
            }
        } catch (IOException e) {
            logger.error("Request handling error : {}",e.getMessage());
        }
    }


    private void checkCookies(DataOutputStream dos,RequestObject requestObject, String path) {
        if(requestObject.getCookies().isEmpty()) {//쿠키 값이 비어있다면, 즉 로그인이 안 돼있으면
            staticFileHandler(dos,FileDetection.fixedPath+"/login/index.html");
        } else if(path.equals("/user/list")){ //유저 목록 정보
            try{
                handleUserListRequest(dos,requestObject);
            } catch(IOException e) {
                logger.debug(e.getMessage());
            }
        } else if(path.equals("/write")) {//로그인이 된 상태에서 접근 시 글 작성 페이지로 이동한다
            staticFileHandler(dos, FileDetection.fixedPath+"/article/index.html");
        }
    }

    //static html 파일은 여기서 다뤄준다
    private void staticFileHandler(DataOutputStream dos, String path) {
        byte[] body;
        File fi = new File(path);

        if(!fi.exists()) {
            response404Header(dos);
            return;
        }
        try(FileInputStream fin = new FileInputStream(fi);
            BufferedInputStream bi = new BufferedInputStream(fin)) {
            body = bi.readAllBytes();
            response200Header(dos,body,path);
        } catch(IOException e) {
            logger.error("Static file handling error: {}" , e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos,byte[] body,String url) throws IOException {
        String contentType = getContentType(url);
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: "+ contentType+";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody(dos,body);
    }

    //응답의 Body섹션
    private void responseBody(DataOutputStream dos, byte[] body) throws IOException{
        dos.write(body, 0, body.length);
        dos.flush();
    }


    //404헤더를 보내준다
    private void response404Header(DataOutputStream dos) {
        try {
            String body = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            byte[] bodyBytes = body.getBytes("UTF-8");
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html ;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
            dos.writeBytes("\r\n");
            responseBody(dos,bodyBytes);
        } catch (IOException e) {
            logger.error("404 response error : {}",e.getMessage());
        }
    }

    //세션을 얻어낸다
    private void handleUserInfoRequest(DataOutputStream dos, RequestObject requestObject) throws IOException {
        String sessionId = requestObject.getCookies().get("SID");
        if (sessionId != null) {// 쿠기 값을 받아 왔다면
            User user = SessionHandler.getUserBySessionId(sessionId);
            if (user != null) {//해당 쿠키 값의 세션 Id랑 일치하는 회원이 존재한다면
                String body = "{ \"username\" : \"" + URLDecoder.decode(user.getUserId(),StandardCharsets.UTF_8) +"\" }";
                //JSON은 key 랑 value가 쌍 따옴표 " 로 둘러 싸져있어야한다.
                byte[] bodyBytes = body.getBytes("UTF-8");
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: application/json;charset=utf-8\r\n"); //json파일로 보낸다
                dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
                dos.writeBytes("\r\n");
                responseBody(dos,bodyBytes);
            }
        }
    }

    //사용자 목록을 출력한다
    private void handleUserListRequest(DataOutputStream dos, RequestObject requestObject) throws IOException {
        StringBuilder sb = new StringBuilder();
        Collection<User> list = new ArrayList<>();
        try{
            list = Database.findAll();
        } catch(Exception e) {
            logger.debug(e.getMessage());
        }

        for(User temp : list){//list에 담겨있는 모든 유저 정보를 StringBuilder에 담아준다
            sb.append(temp).append("<br>");
        }
        String body = "<html><head></head><body>" + sb + " </body></html>";
        byte[] bodyBytes = body.getBytes("UTF-8");
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html ;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody(dos,bodyBytes);
    }

    private void sendBoardList(DataOutputStream dos) {
        try {
            List<Board> boards = Database.getBoards();
            StringBuilder json = new StringBuilder();
            json.append("{ \"boards\": [");
            for (int i = 0; i < boards.size(); i++) {
                Board board = boards.get(i);
                String base64Image = Base64.getEncoder().encodeToString(board.getImage());
                json.append("{");
                json.append("\"title\": \"").append(URLDecoder.decode(board.getTitle(),StandardCharsets.UTF_8)).append("\",");
                json.append("\"content\": \"").append(URLDecoder.decode(board.getContent(),StandardCharsets.UTF_8)).append("\",");
                json.append("\"image\": \"").append(base64Image).append("\"");
                json.append("}");
                if (i < boards.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]}");

            String jsonResponse = json.toString();
            byte[] bodyBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: application/json;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(bodyBytes);
            dos.flush();
        } catch (Exception e) {
            logger.error("Error sending board list: {}", e.getMessage());
        }
    }

    private String getContentType(String url) {
        if (url.endsWith(".css")) return "text/css";
        if (url.endsWith(".svg")) return "image/svg+xml";
        if (url.endsWith(".jpg") || url.endsWith(".jpeg")) return "image/jpeg";
        if (url.endsWith(".png")) return "image/png";
        if (url.endsWith(".ico")) return "image/vnd.microsoft.icon";
        if (url.endsWith(".js")) return "application/javascript";
        return "text/html";
    }
}
