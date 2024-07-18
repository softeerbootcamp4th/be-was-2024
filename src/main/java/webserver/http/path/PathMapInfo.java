package webserver.http.path;

import webserver.api.ReadFileHandler;
import webserver.api.login.LoginHandler;
import webserver.api.logout.LogoutHandler;
import webserver.api.pagehandler.*;
import webserver.api.registration.Registration;
import webserver.api.writepost.WritePost;


/**
 * 초기 pathmap 생성을 위한 클래스
 * <p>
 *     path map에 대한 정보를 기입한다.
 * </p>
 */
public class PathMapInfo {

    static PathMap.PathNode root
            = new PathMap.PathNode("/")
            .Get(MainPageHandler.getInstance())
            .addChild(
                    new PathMap.PathNode("post")
                            .setPathVariable("postid")
                            .Get(MainPageHandler.getInstance())
            )
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
            )
            .addChild(
                    new PathMap.PathNode("write")
                            .Post(WritePost.getInstance())
                            .secured()
            );
}
