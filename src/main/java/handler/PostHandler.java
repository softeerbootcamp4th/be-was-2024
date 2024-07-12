package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler
{
    private PostHandler() {}

    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);

    private static class LazyHolder{
        private static final PostHandler INSTANCE = new PostHandler();
    }
    public static PostHandler getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public void response302Header(DataOutputStream dos, String loc)
    {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+loc+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /*public void responseAlert(DataOutputStream dos, String content) {
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
    }*/
}
