package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class GetHandler
{
    private GetHandler() {}

    private static final Logger logger = LoggerFactory.getLogger(GetHandler.class);
    private static class LazyHolder{
        private static final GetHandler INSTANCE = new GetHandler();
    }
    public static GetHandler getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    //static html 파일은 여기서 다뤄준다
    public void staticFileHandler(DataOutputStream dos, String path)
    {
        byte[] body;
        File fi = new File(path);

        if(!fi.exists())
        {
            response404Header(dos);
            return;
        }

        try(FileInputStream fin = new FileInputStream(fi);
            BufferedInputStream bi = new BufferedInputStream(fin);)
        {
            body = new byte[(int)fi.length()];
            bi.read(body);
            response200Header(dos,body.length,path);
            responseBody(dos, body);
        }
        catch(IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent,String url) {
        try {
            String[] temp = url.split("\\.");
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+match(temp[1])+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    //404헤더를 보내준다
    private void response404Header(DataOutputStream dos)
    {
        try {
            String body = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
            byte[] bodyBytes = body.getBytes("UTF-8");
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html ;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
            dos.writeBytes("\r\n");
            responseBody(dos,bodyBytes);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public String match(String extensions)
    {
        return switch (extensions) {
            case "css" -> "text/css";
            case "svg" -> "image/svg+xml";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "ico" -> "image/vnd.microsoft.icon";
            case "js" -> "text/javascript";
            default -> "text/html";
        };
    }


}
