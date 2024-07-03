package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "./src/main/resources/static";
    private final Socket connection;

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

            String startline = br.readLine();
            HttpRequestHanlder req = new HttpRequestHanlder(startline);

            String headers = startline;
            logger.info("////// request header start //////");
            logger.info(startline);
            while(!headers.isEmpty()){
                headers = br.readLine();
                logger.info(headers);
                String[] parsedline = headers.split(". ");
                if((parsedline.length) ==2) req.addHeader(parsedline[0],parsedline[1]);
            }
            logger.info("////// request header end //////");

            response(dos, req);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response(DataOutputStream dos, HttpRequestHanlder req) {
        try{
            if(req.getUri().contains("?")){ // 쿼리문이 있다면
                String[] path = req.getUri().split("\\?");
                switch (path[0]){
                    case "/registration":
                        registration(path[1]);
                        dos.writeBytes(HttpHeaderBuilder.response302header("http://localhost:8080/"));
                    default:
                        dos.writeBytes(HttpHeaderBuilder.response404Header());
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
                    dos.writeBytes(HttpHeaderBuilder.response404Header());
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


    private void responseByContentType(DataOutputStream dos,byte[] body, String contenttype) throws IOException {
        switch (contenttype) { //content type에 따른 response
            case "html":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "text/html;charset=utf-8"));
                break;
            case "css":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "text/css;charset=utf-8"));
                break;
            case "js":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "text/javascript;charset=utf-8"));
                break;
            case "ico":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "image/x-icon"));
                break;
            case "png":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "image/png"));
                break;
            case "jpg":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "image/jpeg"));
                break;
            case "svg":
                dos.writeBytes(HttpHeaderBuilder.response200Header(body.length, "image/svg+xml"));
                break;
            default:
                dos.writeBytes(HttpHeaderBuilder.response405Header());
                break;
        }
        dos.write(body, 0, body.length);
    }
}
