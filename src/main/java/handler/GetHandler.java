package handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileDetection;
import util.RequestObject;

import java.io.*;
import java.util.Collection;

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

    public void handleGetRequest(DataOutputStream dos, RequestObject requestObject)
    {
        String path = requestObject.getPath();
        if(path.equals("/user/info"))//세션값 요청이 들어왔다면
        {
            try
            {
                handleUserInfoRequest(dos,requestObject);
            }
            catch (IOException e)
            {
                logger.debug("이게 왜 안되는건데");
            }
            return;
        }
        if(path.equals("/user/list"))//사용자 목록 요청이 들어왔을때
        {
            try
            {
                if(requestObject.getCookies().isEmpty())//쿠키 값이 비어 있다면, 즉 로그인 안해있으면
                {
                    staticFileHandler(dos,FileDetection.fixedPath+"/login/index.html");
                    return ;
                }
                handleUserListRequest(dos,requestObject);
            }
            catch (IOException e)
            {
                logger.debug("사용자 목록 예외");
            }
            return;
        }
        path= FileDetection.getPath(FileDetection.fixedPath+path);
        staticFileHandler(dos,path);
    }

    //static html 파일은 여기서 다뤄준다
    private void staticFileHandler(DataOutputStream dos, String path)
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
            response200Header(dos,body,path);
            responseBody(dos, body);
        }
        catch(IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos,byte[] body,String url) {
        try {
            String[] temp = url.split("\\.");
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+match(temp[1])+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
            responseBody(dos,body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //응답의 Body섹션
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


    //세션을 얻어낸다
    private void handleUserInfoRequest(DataOutputStream dos, RequestObject requestObject) throws IOException {
        String sessionId = requestObject.getCookies().get("SID");
        if (sessionId != null) // 쿠기 값을 받아 왔다면
        {
            User user = SessionHandler.getUserBySessionId(sessionId);
            if (user != null)//해당 쿠키 값의 세션 Id랑 일치하는 회원이 존재한다면
            {
                String body = "{ \"username\" : \"" + user.getUserId() +"\" }";
                //JSON은 key 랑 value가 쌍 따옴표 " 로 둘러 싸져있어야한다.
                byte[] bodyBytes = body.getBytes("UTF-8");
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: application/json;charset=utf-8\r\n"); //json파일로 보낸다
                dos.writeBytes("Content-Length: " + body.length() + "\r\n");
                dos.writeBytes("\r\n");
                responseBody(dos,bodyBytes);
            }
        }
    }

    //사용자 목록을 출력한다
    private void handleUserListRequest(DataOutputStream dos, RequestObject requestObject) throws IOException {
        StringBuilder sb = new StringBuilder();
        Collection<User> list = Database.findAll();
        for(User temp : list)//list에 담겨있는 모든 유저 정보를 StringBuilder에 담아준다
        {
            sb.append(temp.toString()+"<br>");
        }
        String body = "<html><head></head><body>" + sb + " </body></html>";
        byte[] bodyBytes = body.getBytes("UTF-8");
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html ;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody(dos,bodyBytes);
    }

    private String match(String extensions)
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
