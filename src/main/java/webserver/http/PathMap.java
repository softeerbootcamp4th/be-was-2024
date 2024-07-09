package webserver.http;

import webserver.api.Logout;
import webserver.api.RequestHandler;
import webserver.api.Login;
import webserver.api.ReadFile;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;

import java.util.HashMap;
import java.util.Map;

public class PathMap {
    private static Map<String, PathNode> pathMap;
    private static RequestHandler readfile = new ReadFile();

    static{
        buildPathMap();
    }

    private static class PathNode {
        Map<Methods, RequestHandler> methods;
        String path;


        public PathNode(String path) {
            this.methods = new HashMap<>();
            this.path = path;
            methods.put(Methods.GET , new ReadFile());
        }

        public RequestHandler getMethod(Methods method) {
            return methods.get(method);
        }
        public String getPath() { return path; }

        public void addGetMethod(RequestHandler getMethod) {
            methods.put(Methods.GET, getMethod);
        }
        public void addPostMethod(RequestHandler postMethod) {
            methods.put(Methods.POST, postMethod);
        }
        public void addPutMethod(RequestHandler putMethod) {
            methods.put(Methods.PUT, putMethod);
        }
        public void addDeleteMethod(RequestHandler deleteMethod) {
            methods.put(Methods.DELETE, deleteMethod);
        }
    }

    public static RequestHandler getPathMethod(Methods method, String path) {
        PathNode current = pathMap.get(path);
        if (current == null) {
            return readfile;
        }
        return current.getMethod(method);
    }


    private static void buildPathMap(){
        pathMap = new HashMap<>();

        //root
        PathNode root = new PathNode("/");
        pathMap.put("/", root);

        //register
        PathNode create = new PathNode("/create");
        create.addPostMethod(new Registration());
        pathMap.put("/create", create);

        //login
        PathNode login = new PathNode("/login");
        login.addPostMethod(new Login());
        pathMap.put("/login", login);

        //logout
        PathNode logout = new PathNode("/logout");
        logout.addGetMethod(new Logout());
        pathMap.put("/logout", logout);
    }
}
