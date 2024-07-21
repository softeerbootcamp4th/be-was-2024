package handler;

import db.Database;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.UserProcessor;
import util.RequestObject;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Post메소드로 들어온 모든 요청을 다뤄주는 클래스
 */
public class PostHandler {


    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private final UserProcessor userProcessor;

    private PostHandler() {
        this.userProcessor=UserProcessor.getInstance();
    }

    private static class LazyHolder{
        private static final PostHandler INSTANCE = new PostHandler();
    }

    /**
     * LazyHolder방식으로 싱글톤 구현 위한 클래스
     */
    public static PostHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * Post메소드로 들어온 요청의 경로를 파싱해서 해당하는 메소드를 호출해주는 클래스
     * @param dos DataOutputStream 객체
     * @param requestObject
     */
    public void handlePostRequest(DataOutputStream dos, RequestObject requestObject) {
        String path = requestObject.getPath();

        //switch로 리팩토링 , 메소드 빼놓기
        switch(path){
            case "/user/create" -> userCreate(dos,requestObject);
            case "/user/login" -> userLogin(dos,requestObject);
            case "/write" -> boardWrite(dos,requestObject);
        }
    }

    private void userLogin(DataOutputStream dos, RequestObject requestObject) {
        try {
            User user = userProcessor.userFind(requestObject);
            loginSuccess(dos,user);//로그인 성공 시
        } catch(Exception e) {//해당하는 예외 메세지를 출력한다
            responseAlert(dos,e.getMessage(),"/login/index.html");
        }
    }

    private void userCreate(DataOutputStream dos, RequestObject requestObject) {
        userProcessor.userCreate(requestObject);
        response302Header(dos,"/index.html");
    }

    private void boardWrite(DataOutputStream dos, RequestObject requestObject) {

        String title = requestObject.getParsedTitle();
        String content = requestObject.getParsedContent();
        byte[] image = requestObject.getParsedImage();
        if (title != null && content != null && image != null) {
            try{
                Database.addBoard(new Board(title,content,image));
            } catch(Exception e)
            {
                logger.debug(e.getMessage());
            }
        }
        response302Header(dos, "/index.html");
    }//작성 후 메인페이지로 넘어감



    private void response302Header(DataOutputStream dos, String loc) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+loc+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseAlert(DataOutputStream dos, String content, String location) {
        try {
            String body = "<html><head><script type='text/javascript'>alert('" + content + "');window.location='"+location+"';</script></head></html>";
            byte[] bodyBytes = body.getBytes("UTF-8");
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(bodyBytes);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void loginSuccess(DataOutputStream dos, User user) {
        try {
            String session = SessionHandler.createSession(user);
            logger.error(session);
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("Set-Cookie: SID=" + session + "; Path=/; \r\n");
            dos.writeBytes("\r\n");
        } catch(IOException e) {
            logger.error(e.getMessage());
        }
    }
}
