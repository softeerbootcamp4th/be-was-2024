package webserver.http.path;

import webserver.api.ReadFileHandler;
import webserver.api.login.LoginHandler;
import webserver.api.logout.LogoutHandler;
import webserver.api.pagehandler.*;
import webserver.api.registration.Registration;

public class PathMapInfo {

    static PathMap.PathNode root
            = new PathMap.PathNode("/")
            .Get(MainPageHandler.getInstance())
            .addChild(
                    new PathMap.PathNode("registration")
                            .Get(RegistrationPageHandler.getInstance())
            )
            .addChild(
                    new PathMap.PathNode("create")
                            .Post(Registration.getInstance())
            )
            .addChild(
                    new PathMap.PathNode("login")
                            .Get(LoginPageHandler.getInstance())
                            .Post(LoginHandler.getInstance())
            )
            .addChild(
                    new PathMap.PathNode("logout")
                            .Get(LogoutHandler.getInstance())
            )
            .addChild(
                    new PathMap.PathNode("user").secured()
                            .addChild(
                                    new PathMap.PathNode("list")
                                            .Get(UserListPageHandler.getInstance())
                            )
            )
            .addChild(
                    new PathMap.PathNode("article")
                            .Get(WritePageHandler.getInstance())
                            .secured()
            )
            .addChild(
                    new PathMap.PathNode("resource")
                            .Get(ReadFileHandler.getInstance())
            );
}
