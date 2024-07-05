package webserver;

import java.io.*;
import java.net.Socket;
import java.io.File;

import db.Database;
import exception.QueryParameterNotFoundException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private final String staticResourcePath = "src/main/resources/static";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = HttpRequest.read(in);
            logger.debug(httpRequest.toString());

            HttpResponse httpResponse = doServiceLogic(httpRequest);

            response(new DataOutputStream(out), httpResponse);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse doServiceLogic(HttpRequest httpRequest) {
        switch (httpRequest.getPath()) {
            case "/create":
                return registration(httpRequest);
            default:
                return defaultLogic(httpRequest);
        }
    }

    private HttpResponse defaultLogic(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        File file = new File(staticResourcePath + request.getViewPath());

        if (!file.exists()) {
            return HttpResponse.error(HttpStatus.SC_NOT_FOUND, "Page Not Found!");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = fis.readAllBytes();
            response.setBody(body);
            response.setStatusCode(HttpStatus.SC_OK);
            response.addHeader("Content-Type", request.getContentType());
            response.addHeader("Content-Length", String.valueOf(body.length));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return HttpResponse.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Server Error!");
        }

        return response;
    }

    private HttpResponse registration(HttpRequest httpRequest){
        HttpResponse response = new HttpResponse();
        try{
            String userId = httpRequest.getQueryParameterValue("userId");
            String password = httpRequest.getQueryParameterValue("password");
            String name = httpRequest.getQueryParameterValue("name");
            String email = httpRequest.getQueryParameterValue("email");

            Database.addUser(new User(userId, password, name, email));

            return HttpResponse.redirect("/index.html");
        } catch (QueryParameterNotFoundException qe){
            // /registration/index.html form의 내용은 전부 required 이므로 유저가 임의로 url을 변경하여 접근했을때이다.
            logger.debug(qe.getMessage());
            return HttpResponse.error(HttpStatus.SC_BAD_REQUEST, "Invalid Access");
        }
    }


    private void response(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpResponse.headersToString());
        byte[] body = httpResponse.getBody();
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
