package logic;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpMethod;
import model.enums.HttpStatus;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.constant.StringConstants.*;

public class Logics {
    public static HttpResponse create(HttpRequest httpRequest) throws IOException {
        if(!httpRequest.getHttpMethod().equals(HttpMethod.POST)){
            throw new RuntimeException("Invalid method");
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        // body
        String bodyToString = new String(httpRequest.getBody(), StandardCharsets.UTF_8);
        String[] bodyParsedPairList = bodyToString.split(AMPERSAND);

        Map<String, String> parsingBodyParams = new HashMap<>();
        for (String bodyParsedPair : bodyParsedPairList) {
            String queryKey = bodyParsedPair.substring(0, bodyParsedPair.indexOf(EQUAL));
            String queryValue = bodyParsedPair.substring(bodyParsedPair.indexOf(EQUAL));
            parsingBodyParams.put(queryKey, queryValue);
        }
        String userId = URLDecoder.decode(parsingBodyParams.get("userId"), UTF_8);
        String password = URLDecoder.decode(parsingBodyParams.get("password"), UTF_8);
        String username = URLDecoder.decode(parsingBodyParams.get("name"), UTF_8);
        String email = URLDecoder.decode(parsingBodyParams.get("email"), UTF_8);

        User user = new User(userId, password, username, email);
        Database.addUser(user);
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
        //TODO : body를 리턴하지 않아도 되는가
    }
}
