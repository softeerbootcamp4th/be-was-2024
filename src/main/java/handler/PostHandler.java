package handler;

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
        }
        else if(requestObject.getPath().equals("/user/login"))
        {
            if(!userProcessor.findUser(requestObject))//해당하는 사용자가 없다면
            {
                logger.debug("False, 해당 Id는 존재하지 않는 Id입니다");
            }
            else
            {
                logger.debug("True, 해당하는 사용자가 있습니다");
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

    public void responseAlert(DataOutputStream dos, String content) {
        try {
            String body = "<html><head><script type='text/javascript'>alert('" + content + "');window.location='/login/index.html';</script></head></html>";
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
}
