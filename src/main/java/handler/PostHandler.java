package handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.UserProcessor;
import util.RequestObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler
{


    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private final UserProcessor userProcessor;

    private PostHandler() {
        this.userProcessor=UserProcessor.getInstance();
    }

    private static class LazyHolder{
        private static final PostHandler INSTANCE = new PostHandler();
    }
    public static PostHandler getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public void handlePostRequest(DataOutputStream dos, RequestObject requestObject) {

        if(requestObject.getPath().equals("/user/create"))
        {
            userProcessor.userCreate(requestObject);
            response302Header(dos,"/index.html");
        }
        else if(requestObject.getPath().equals("/user/login"))
        {
            try
            {
                User user = userProcessor.findUser(requestObject);
                loginSuccess(dos,user);//로그인 성공 시
            }
            catch(Exception e)//해당하는 예외 메세지를 출력한다
            {
                responseAlert(dos,e.getMessage(),"/login/index.html");
            }
        }
    }

    private void response302Header(DataOutputStream dos, String loc)
    {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+loc+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseAlert(DataOutputStream dos, String content, String location) {
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

    public void loginSuccess(DataOutputStream dos, User user)
    {
        try
        {
            String session = SessionHandler.createSession(user);
            logger.error(session);
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("Set-Cookie: SID=" + session + "; Path=/; \r\n");
            dos.writeBytes("\r\n");
        }
        catch(IOException e)
        {
            logger.error(e.getMessage());
        }

    }
}
