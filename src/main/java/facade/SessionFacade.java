package facade;

import db.SessionDatabase;
import web.HttpRequest;

import java.util.Map;

public class SessionFacade {

    private static final String SESSION_ID = "SID";

    public static boolean authenticateRequest(HttpRequest request) {
        Map<String, String> cookie = request.getCookie();
        String SID = cookie.get(SESSION_ID);

        SessionDatabase.removeExpiredSessions();

        return SessionDatabase.isValidateSession(SID);
    }

    public static void invalidateAndRemoveSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        SessionDatabase.invalidateSession(SID);
        SessionDatabase.removeExpiredSessions();
    }

}

