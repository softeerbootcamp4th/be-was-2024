package util;

import db.SessionDatabase;
import dto.HttpRequest;
import dto.HttpResponse;
import model.Session;
import util.constant.StringConstants;

import java.io.IOException;

public class HttpResponseConverter {


    // httpRequest 를 httpResponse 로 변경하는 로직
    public HttpResponse with(HttpRequest httpRequest) throws IOException {
        String sessionId = httpRequest.getSessionOrNull().orElse(null);
        String userId = null;
        if (sessionId != null) {
            userId = SessionDatabase.findSessionById(sessionId).map(Session::getUserId).orElse(null);
        }

        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest, userId);

    }
}
