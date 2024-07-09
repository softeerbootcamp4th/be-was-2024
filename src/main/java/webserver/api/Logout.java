package webserver.api;

import webserver.http.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.session.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Logout implements RequestHandler {
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {

        String cookies = request.getHeaders().get("Cookie");
        if(cookies != null) {
            String[] line = cookies.split(";");
            for(String cookie : line) {
                String[] keyValue = cookie.split("=");
                if(keyValue.length == 2) {
                    if(keyValue[0].equals("sid")) {
                        Session.deleteSession(keyValue[1]);
                    }
                }
            }
        }

        byte[] responseBody = Files.readAllBytes(new File(HtmlFiles.main_page).toPath());
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", Extension.HTML.getContentType())
                .addheader("Content-Length", String.valueOf(responseBody.length))
                .addheader("Set-Cookie","sid=deleted; Path=/")
                .setBody(responseBody)
                .build();
    }
}
