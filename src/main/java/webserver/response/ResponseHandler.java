package webserver.response;

import db.Database;
import model.User;
import webserver.request.Parameter;
import webserver.request.Path;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static util.Utils.getContentType;
import static util.Utils.getFile;

public class ResponseHandler {

    public static void response(OutputStream outputStream, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(outputStream);
        dos.write(httpResponse.toByte());
    }

    public static void responseStaticContents(OutputStream out, Path path) throws IOException {
        byte[] body = getFile(path.get());
        HttpResponse response = new HttpResponse(200, body);
        response.addHeader("Content-Type", getContentType(path.getExtension())+";charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        ResponseHandler.response(out, response);
    }

    public static void responseDynamicContents(OutputStream out, Path path) throws IOException {

        switch(path.get()){
            case "/registration":
                byte[] body = "".getBytes();
                HttpResponse response = new HttpResponse(303, body);
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
                break;
        }

    }

}
