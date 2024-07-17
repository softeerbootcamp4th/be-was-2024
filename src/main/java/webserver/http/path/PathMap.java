package webserver.http.path;

import webserver.api.*;
import webserver.http.HttpRequest;
import webserver.http.enums.Methods;
import webserver.session.SessionDAO;

import java.util.HashMap;
import java.util.Map;

public class PathMap {
    private static final PathNode root = PathMapInfo.root;

    static class PathNode {
        private final Map<String, PathNode> children;
        private final Map<Methods, FunctionHandler> methods;
        private final String path;
        private String pathvariable;
        private boolean secured;


        public PathNode(String path) {
            this.methods = new HashMap<>();
            this.children = new HashMap<>();
            this.path = path;
            this.secured = false;
            this.pathvariable = null;
        }

        PathNode secured(){
            this.secured = true;
            return this;
        }

        PathNode setPathVariable(String pathvariable){
            this.pathvariable = pathvariable;
            return this;
        }

        PathNode addChild(PathNode node){
            if(this.secured) node.secured();
            children.put(node.getPath(), node);
            return this;
        }

         PathNode Get(FunctionHandler getMethod) {
            methods.put(Methods.GET, getMethod);
            return this;
        }
         PathNode Post(FunctionHandler postMethod) {
            methods.put(Methods.POST, postMethod);
            return this;
        }
         PathNode Put(FunctionHandler putMethod) {
            methods.put(Methods.PUT, putMethod);
            return this;
        }
         PathNode Delete(FunctionHandler deleteMethod) {
            methods.put(Methods.DELETE, deleteMethod);
            return this;
        }

        //getter
        public FunctionHandler getMethod(Methods method) {
            return methods.get(method);
        }

        public String getPath() { return path; }

        public String getPathvariable() {
            return pathvariable;
        }

        public PathNode findChild(String pathname) {
            return children.get(pathname);
        }
    }

    public static FunctionHandler getPathMethod(HttpRequest request) {
        String[] routes = request.getUrl().getPath().split("/");
        PathNode current = root;
        if(routes.length > 1){
            for(int i = 1; i < routes.length; i++){
                if(current.getPathvariable()!=null ){
                    request.addPathVariable(current.getPathvariable(), routes[i]);
                    if(++i >= routes.length){
                        break;
                    }
                }
                PathNode next = current.findChild(routes[i]);
                if(next == null) {
                    break;
                }
                current = next;
            }
        }

        //secured but no session
        SessionDAO sessionDAO = new SessionDAO();
        if(current.secured){
            if(request.getSessionid() == null || sessionDAO.findSession(request.getSessionid()) == null)
                return Unauthorized.getInstance();
        }

        return current.getMethod(request.getMethod());
    }
}
