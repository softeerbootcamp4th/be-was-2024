package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PathPar;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private PathPar pathPar = new PathPar();
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
            String url = pathPar.getUrl(line);
            Map<String,String> queryParams=pathPar.getQueryParams(line);
            while(!line.equals(""))
            {
                line = br.readLine();
                logger.debug("request Headers : {}",line);
            }
            //week1 task2 를 위한 주석
            for(Map.Entry<String,String> entry : queryParams.entrySet())
            {
                System.out.println(entry.getKey()+" : "+entry.getValue());
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);


            byte[] body;
            url = pathPar.getDir("src/main/resources/static"+url);
            File fi = new File(url);
            try(FileInputStream fin = new FileInputStream(fi);
                BufferedInputStream bi = new BufferedInputStream(fin);)
            {
                body = new byte[(int)fi.length()];
                bi.read(body);
            }

            response200Header(dos,body.length,url);
            responseBody(dos, body);
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
