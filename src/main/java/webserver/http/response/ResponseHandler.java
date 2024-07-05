package webserver.http.response;

import db.Database;
import model.User;
import webserver.http.request.Request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static util.Utils.getContentType;
import static util.Utils.getFile;

public class ResponseHandler {

    public static void response(OutputStream outputStream, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        dos.write(response.toByte());
    }

    public static void responseStaticContents(OutputStream out, Request request) throws IOException {
        byte[] body = getFile(request.getPath());
        Response response = new Response(200, body);
        response.addHeader("Content-Type", getContentType(request.getExtension())+";charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        ResponseHandler.response(out, response);
    }

    public static void responseDynamicContents(OutputStream out, Request request) throws IOException {

        switch(request.getPath()){
            case "/registration":
                byte[] body = "".getBytes();
                Response response = new Response(303, body);
                response.addHeader("Content-Length", String.valueOf(body.length));
                response.addHeader("Location", "/registration/index.html");
                ResponseHandler.response(out, response);
                break;

            case "/create":
                User user = new User(
                        request.getParameterValue("userId"),
                        request.getParameterValue("password"),
                        request.getParameterValue("name"),
                        request.getParameterValue("email")
                );
                Database.addUser(user);
                break;
        }

    }

}
