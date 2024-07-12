package webserver.annotation.processor;

import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.util.Map;

public class LoginCheckProcessor {
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    public boolean isUserLoggedIn(MyHttpRequest httpRequest) {
        return httpRequestParser.isLogin(httpRequest);
    }

    public MyHttpResponse toLoginPage() {
        return new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/login"));
    }
}


