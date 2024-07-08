package webserver.http;

import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;

import java.util.HashMap;
import java.util.Map;

public class PathMap {
    private static PathNode root;

    static{
        buildPathMap();
    }

        private static class PathNode {
            String nodename;

            Map<Methods, ApiFunction> methods;

            Map<String, PathNode> childnode;

        public PathNode(String nodename) {
            this.nodename = nodename;
            this.methods = new HashMap<>();
            this.childnode = new HashMap<>();
            methods.put(Methods.GET , new ReadFile());
        }

        public ApiFunction getMethod(Methods method) {
            return methods.get(method);
        }

        public PathNode getChild(String nodename) {
            return childnode.get(nodename);
        }

        public PathNode addChild(String nodename) {
            PathNode child = new PathNode(nodename);
            childnode.put(nodename, child);
            return child;
        }

        public void addGetMethod(ApiFunction getMethod) {
            methods.put(Methods.GET, getMethod);
        }
        public void addPostMethod(ApiFunction postMethod) {
            methods.put(Methods.POST, postMethod);
        }
        public void addPutMethod(ApiFunction putMethod) {
            methods.put(Methods.PUT, putMethod);
        }
        public void addDeleteMethod(ApiFunction deleteMethod) {
            methods.put(Methods.DELETE, deleteMethod);
        }
    }

    public static PathNode getRoot() {
        return root;
    }

    public static ApiFunction getPathMethod(Methods method, String path) {
        String[] routes = path.split("/");
        PathNode current = root;
        if(routes.length == 1){
            return current.getMethod(method);
        }
        for(int i = 1; i < routes.length; i++){
            PathNode next = current.getChild(routes[i]);
            if(next == null) break;
            current = next;
        }
        return current.getMethod(method);
    }


    private static void buildPathMap(){
        root = new PathNode("root");

        //  /registration/~
        root.addChild("create").addPostMethod(new Registration());
    }
}
