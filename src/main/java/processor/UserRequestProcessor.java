package processor;

import model.User;
import type.MIMEType;
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

        HashMap<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", MIMEType.HTML.getContentType());
        if (userId == null || name == null && password == null ||
                userId.isEmpty() || name.isEmpty() || password.isEmpty()) {

            setResult(StatusCodeType.BAD_REQUEST, responseHeader, "" +
                    "<script>" +
                    "alert('Fill all the required fields');" +
                    "location.href='/registration'" +
                    "</script>");
            return;
        }

        User user = new User(userId, name, password, email);
        responseHeader.put("Location", "/index.html");
        setResult(StatusCodeType.OK, responseHeader,"" +
                "<script>" +
                "alert('success');" +
                "location.href='/';" +
                "</script>");
    }
}
