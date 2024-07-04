package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.UserProcessor;
import util.FileDetection;
import util.RequestObject;

import javax.xml.crypto.Data;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private RequestObject requestObject ;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String line = br.readLine();
            logger.debug("request line : {} ",line);// 가장 첫번째 줄, 즉 request line
            requestObject = new RequestObject(line);
            while(!line.equals(""))
            {
                line = br.readLine();
                logger.debug("request Headers : {}",line);
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            frontRequest(dos,requestObject);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void frontRequest(DataOutputStream dos,RequestObject requestObject)
    {
        String method = requestObject.getMethod();
        String path = requestObject.getPath();
        if(method.equals("GET") && path.equals("/user/create"))//GET방식의 회원가입 일 시
        {
            UserProcessor.userCreate(requestObject);
            responseAlert(dos,"로그인 성공");
            return ;
        }

        // 파일 요청이 들어왔다면 경로를 디렉토리인지 아닌지 판단해서 다시 설정해준다
        path = FileDetection.getPath(FileDetection.fixedPath + path) ;
        staticFileHandler(dos,path);
    }


    //static html 파일은 여기서 다뤄준다
    private void staticFileHandler(DataOutputStream dos,String path)
    {
        byte[] body;
        File fi = new File(path);
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

    private void response302Header(DataOutputStream dos,String loc)
    {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+loc+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
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

    private void responseAlert(DataOutputStream dos, String content) {
        try {
            String body = "<html><head><script type='text/javascript'>alert('" + content + "');window.location='/login/index.html';</script></head></html>";
            byte[] bodyBytes = body.getBytes("UTF-8");
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + bodyBytes.length + "\r\n");
            dos.writeBytes("\r\n");
            dos.write(bodyBytes);
            dos.flush();
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
