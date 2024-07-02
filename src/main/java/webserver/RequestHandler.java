package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "./src/main/resources/static";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            InputStreamReader dis = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(dis);

            Map<String, String> header = new HashMap<>();


            logger.info("////// request header start //////");
            String line = br.readLine();
            String[] startline = line.split(" ");
            logger.info(line);
            while(!line.isEmpty()){
                line = br.readLine();
                String[] parsedline = line.split(". ");
                if((parsedline.length) ==2) header.put(parsedline[0], parsedline[1]);
            }
            logger.info(header.toString());
            logger.info("////// request header end //////");

            responseByPath(dos, startline, header);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseByPath(DataOutputStream dos, String[] startline, Map header) throws IOException {

        if(startline[1].contains("?")){
            String[] path = startline[1].split("\\?");
            switch (path[0]){
                case "/registration":
                    registration(path[1]);
                    redirection(dos, "http://localhost:8080/");
                default:
                    response404Header(dos);
            }
        }else{
            if(!startline[1].contains(".")) startline[1] = startline[1] + "/index.html";
            String pathname = root + startline[1];
            byte[] body = Files.readAllBytes(new File(pathname).toPath());
            responseByContentType(dos, body, startline[1].split("\\.")[1]);
        }
    }

    private void registration (String params){ //사용자 등록
        String id = "";
        String username = "";
        String password = "";
        for(String line : params.split("&")){
            String[] tmp = line.split("=");
            if(Objects.equals(tmp[0], "id")){
                id = tmp[1];
            }else if(Objects.equals(tmp[0], "username")){
                username = tmp[1];
            }else if(Objects.equals(tmp[0], "password")){
                password = tmp[1];
            }
        }
        WebServer.addUser(id, username, password,"");
    }

    private void redirection(DataOutputStream dos, String redirectpath){ //redirection
        try {
            dos.writeBytes("HTTP/1.1 302 \r\n");
            dos.writeBytes("Location: " + redirectpath+ "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseByContentType(DataOutputStream dos,byte[] body, String contenttype){
        switch (contenttype) { //content type에 따른 response
            case "html":
                response200Header(dos, body.length,"text/html;charset=utf-8");
                responseBody(dos, body);
                break;
            case "css":
                response200Header(dos, body.length,"text/css;charset=utf-8");
                responseBody(dos, body);
                break;
            case "js":
                response200Header(dos, body.length,"text/javascript;charset=utf-8");
                responseBody(dos, body);
                break;
            case "ico":
                response200Header(dos, body.length,"image/x-icon");
                responseBody(dos, body);
                break;
            case "png":
                response200Header(dos, body.length,"image/png");
                responseBody(dos, body);
                break;
            case "jpg":
                response200Header(dos, body.length,"image/jpeg");
                responseBody(dos, body);
                break;
            case "svg":
                response200Header(dos, body.length,"image/svg+xml");
                responseBody(dos,body);
                break;
            default:
                response405Header(dos);
                break;
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String ContentType) {

        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + ContentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos) {
        try{
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private void response405Header(DataOutputStream dos) {
        try{
            dos.writeBytes("HTTP/1.1 405 Unsupported content type\r\n");
        } catch (IOException e){
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
}
