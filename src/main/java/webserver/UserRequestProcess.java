package webserver;

import handler.UserHandler;
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

    public void createUser(HttpRequestObject httpRequestObject) {
        userHandler.createUser(httpRequestObject.getRequestParams());
    }
}
