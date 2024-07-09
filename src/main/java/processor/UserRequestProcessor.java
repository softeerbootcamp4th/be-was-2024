package processor;

import db.Database;
import exception.StatusCodeException;
import model.User;
import type.MIMEType;
import type.StatusCodeType;
import utils.StringUtils;
import webserver.RequestInfo;

import java.util.HashMap;

public class UserRequestProcessor extends RequestProcessor {

    public UserRequestProcessor(RequestInfo requestInfo) throws StatusCodeException {
        init(requestInfo);

        switch (methodPath()) {
            case "POST /create" -> createUser();
            default -> throw new StatusCodeException(StatusCodeType.NOT_FOUND);
        }
    }

    // POST "/create"
    private void createUser() {
        HashMap<String, String> param = StringUtils.param2Map(getBody());
        String userId = param.get("userId");
        String name = param.get("name");
        String password = param.get("password");
        String email = param.get("email");

        if (userId == null || name == null || password == null || email == null ||
                userId.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty()) {

            insert2ResponseHeader("Content-Type", MIMEType.HTML.getContentType());
            setResult(StatusCodeType.BAD_REQUEST, getResponseHeader(), "" +
                    "<script>" +
                    "alert('Fill all the required fields');" +
                    "location.href='/registration'" +
                    "</script>");

            return;
        }

        User user = new User(userId, name, password, email);
        Database.addUser(user);
        insert2ResponseHeader("Location", "/index.html");
        setResult(StatusCodeType.FOUND, getResponseHeader(), "");
    }
}
