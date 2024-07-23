package util;

import db.Database;
import db.SessionDB;
import dto.HttpRequest;
import dto.HttpResponse;
import model.Session;

import java.io.IOException;

public class HttpResponseConverter {
    private static final Database<Session,String> sessionDatabase = SessionDB.getInstance();



    // httpRequest 를 httpResponse 로 변경하는 로직
    public HttpResponse with(HttpRequest httpRequest) throws IOException {
        String sessionId = httpRequest.getSessionOrNull().orElse(null);
        String userId = null;
        if (sessionId != null) {
            userId = sessionDatabase.findById(sessionId).map(Session::getUserId).orElse(null);
        }

        HttpPathMapper httpPathMapper = new HttpPathMapper();
        return httpPathMapper.map(httpRequest, userId);

    }
}
