package util;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpPathMapper {
    private static final Logger logger = LoggerFactory.getLogger(HttpPathMapper.class);

    public HttpResponse map(HttpRequest httpRequest) throws IOException {
        logger.info("httpRequest.getPath() = " + httpRequest.getPath());

        return switch (httpRequest.getPath()) {
            case "/create" -> create(httpRequest);

            default -> throw new RuntimeException("Invalid path");
        };

    }

    private HttpResponse create(HttpRequest httpRequest) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");
        String userId = URLDecoder.decode(httpRequest.getQueryParams().get("userId"), "UTF-8");
        String password = URLDecoder.decode(httpRequest.getQueryParams().get("password"), "UTF-8");
        String username = URLDecoder.decode(httpRequest.getQueryParams().get("name"), "UTF-8");
        String email = URLDecoder.decode(httpRequest.getQueryParams().get("email"), "UTF-8");

        User user = new User(userId, username, password, email);
        Database.addUser(user);
        return HttpResponse.of("HTTP/1.1", HttpStatus.SEE_OTHER, headers, new byte[0]);
    }
}
