package webserver;

import handler.UserHandler;
import model.User;
import util.HttpRequestObject;

public class UserRequestProcess {

    private static UserRequestProcess instance = null;
    private final UserHandler userHandler;

    public static UserRequestProcess getInstance() {
        if (instance == null) {
            instance = new UserRequestProcess();
        }
        return instance;
    }

    private UserRequestProcess() {
        this.userHandler = UserHandler.getInstance();
    }

    public User createUser(HttpRequestObject httpRequestObject) {
        return userHandler.createUser(httpRequestObject.getRequestParams());
    }
}
