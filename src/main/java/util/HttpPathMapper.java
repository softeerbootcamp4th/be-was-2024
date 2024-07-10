package util;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpMethod;
import model.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        if(!httpRequest.getHttpMethod().equals(HttpMethod.POST)){
            throw new RuntimeException("Invalid method");
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        // body
        String bodyToString = new String(httpRequest.getBody(), StandardCharsets.UTF_8);
        String[] bodyParsedPairList = bodyToString.split("&");

        Map<String, String> parsingBodyParams = new HashMap<>();
        for (String bodyParsedPair : bodyParsedPairList) {
            String queryKey = bodyParsedPair.substring(0, bodyParsedPair.indexOf("="));
            String queryValue = bodyParsedPair.substring(bodyParsedPair.indexOf("="));
            parsingBodyParams.put(queryKey, queryValue);
        }

        //save
        String userId = URLDecoder.decode(parsingBodyParams.get("userId"), "UTF-8");
        String password = URLDecoder.decode(parsingBodyParams.get("password"), "UTF-8");
        String username = URLDecoder.decode(parsingBodyParams.get("name"), "UTF-8");
        String email = URLDecoder.decode(parsingBodyParams.get("email"), "UTF-8");

        User user = new User(userId, password, username, email);
        Database.addUser(user);
        return HttpResponse.of("HTTP/1.1", HttpStatus.SEE_OTHER, headers, new byte[0]);
        //TODO : body를 리턴하지 않아도 되는가
    }
}
