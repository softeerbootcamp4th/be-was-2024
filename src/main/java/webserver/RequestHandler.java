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

    private class httprequest {
        private String method;
        private String uri;
        private String body;
        private String protocol;
        private Map<String, String> headers = new HashMap<>();
        

        public httprequest(BufferedReader br) {
            try{
                logger.info("////// request header start //////");
                String line = br.readLine();
                String[] startline = line.split(" ");
                logger.info(line);
                method =  startline[0];
                uri = startline[1];
                protocol = startline[2];
                while(!line.isEmpty()){
                    line = br.readLine();
                    String[] parsedline = line.split(". ");
                    if((parsedline.length) ==2) headers.put(parsedline[0], parsedline[1]);
                }
                logger.info("////// request header end //////");

            }catch (IOException e){
                logger.error(e.getMessage());
            }
        }

        public String getMethod() {
            return method;
        }

        public String getUri() {
            return uri;
        }

        public String getBody() {
            return body;
        }

        public String getProtocol() {
            return protocol;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }
    }

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

            httprequest req = new httprequest(br);

            response(dos, req);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response(DataOutputStream dos, httprequest req) {
        try{
            if(req.getUri().contains("?")){ // 쿼리문이 있다면
                String[] path = req.getUri().split("\\?");
                switch (path[0]){
                    case "/registration":
                        registration(path[1]);
                        new responseBuilder(dos).response302header("http://localhost:8080/");
                    default:
                        new responseBuilder(dos).response404Header();
                }
            }else{
                String[] path = req.getUri().split("/");
                String pathname = root + req.getUri();
                String extension = pathname.substring(pathname.lastIndexOf("/")); // 마지막 부분의 확장자 확인
                if(!extension.contains(".")) { // 확장자가 있다면
                    pathname = root + req.getUri() + "/index.html";
                    extension = "html";
                }else{
                    extension = extension.split("\\.")[1];
                }
                byte[] body = Files.readAllBytes(new File(pathname).toPath());
                if(body.length >0 ){
                    responseByContentType(dos, body, extension);
                }
                else
                    new responseBuilder(dos).response404Header();
            }
        }catch (IOException e){
            logger.error(e.getMessage());
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



    private void responseByContentType(DataOutputStream dos,byte[] body, String contenttype){
        logger.info(contenttype);
        switch (contenttype) { //content type에 따른 response
            case "html":
                new responseBuilder(dos).response200Header(body.length, "text/html;charset=utf-8",body);
                break;
            case "css":
                new responseBuilder(dos).response200Header(body.length, "text/css;charset=utf-8",body);
                break;
            case "js":
                new responseBuilder(dos).response200Header(body.length, "text/javascript;charset=utf-8",body);
                break;
            case "ico":
                new responseBuilder(dos).response200Header(body.length, "image/x-icon",body);
                break;
            case "png":
                new responseBuilder(dos).response200Header(body.length, "image/png",body);
                break;
            case "jpg":
                new responseBuilder(dos).response200Header(body.length, "image/jpeg",body);
                break;
            case "svg":
                new responseBuilder(dos).response200Header(body.length, "image/svg+xml",body);
                break;
            default:
                new responseBuilder(dos).response405Header();
                break;
        }
    }


    private static class responseBuilder{
        private DataOutputStream dos;
        public responseBuilder(DataOutputStream dos) {
            this.dos = dos;
        }

        public void response302header(String redirectpath){ //redirection
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

        public void response200Header(int lengthOfBodyContent, String ContentType, byte[] body) {
            try {
                dos.writeBytes("HTTP/1.1 200 OK \r\n");
                dos.writeBytes("Content-Type: " + ContentType + "\r\n");
                dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
                dos.writeBytes("\r\n");
                responseBody(body);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        public void response404Header() {
            try{
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                dos.flush();
            } catch (IOException e){
                logger.error(e.getMessage());
            }
        }

        public void response404Header(byte[] body) {
            try{
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                responseBody(body);
            } catch (IOException e){
                logger.error(e.getMessage());
            }
        }

        public void response405Header() {
            try{
                dos.writeBytes("HTTP/1.1 405 Unsupported content type\r\n");
                dos.flush();
            } catch (IOException e){
                logger.error(e.getMessage());
            }
        }

        private void responseBody(byte[] body) {
            try {
                dos.write(body, 0, body.length);
                dos.flush();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

    }





}
