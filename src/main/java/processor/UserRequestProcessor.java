package processor;

import model.User;
import type.StatusCodeType;

import java.util.HashMap;

public class UserRequestProcessor extends RequestProcessor {

    public UserRequestProcessor(String method, String path, HashMap<String, String> query) {
        init(method, path, query);

        switch (method + " " + path) {
            case "GET /create" -> createUser();
//            default
        }
    }

    // GET "/create?"
    private void createUser() {
        String userId = getQuery().get("userId");
        String name = getQuery().get("name");
        String password = getQuery().get("password");
        String email = getQuery().get("email");
        User user = new User(userId, name, password, email);

        setResult(StatusCodeType.FOUND, "/");
    }
}
