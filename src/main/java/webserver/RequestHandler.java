package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.Path;
import webserver.request.Parameter;
import webserver.response.HttpResponse;
import webserver.response.ResponseHandler;

import static util.Utils.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO httpRequest에 toString을 적용해서 아래 로직을 없애기
            String request = convert(in);
            logger.debug(request);

            HttpRequest httpRequest = new HttpRequest(request);
            Path path = httpRequest.getPath();

            if(httpRequest.getPath().isStatic()){

                byte[] body = getFile(path.get());
                HttpResponse response = new HttpResponse(200, body);
                response.addHeader("Content-Type", getContentType(path.getExtension())+";charset=utf-8");
                response.addHeader("Content-Length", String.valueOf(body.length));
                ResponseHandler.response(out, response);
            }else {
                switch(path.get()){
                    case "/registration":
                        byte[] body = "".getBytes();
                        HttpResponse response = new HttpResponse(302, body);
                        response.addHeader("Content-Length", String.valueOf(body.length));
                        response.addHeader("Location", "/registration/index.html");

                        ResponseHandler.response(out, response);
                        break;

                    case "/create":
                        Parameter parameter = path.getParameter().get();
                        User user = new User(
                                parameter.get("userId"),
                                parameter.get("password"),
                                parameter.get("name"),
                                parameter.get("email")
                        );
                        Database.addUser(user);
                        System.out.println(user);
                        break;
                }
            }


        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String convert(InputStream inputStream) throws IOException {
        return getAllStrings(inputStream);
    }

}
