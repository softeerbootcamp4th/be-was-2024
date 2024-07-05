package webserver.http;

import webserver.api.ApiFunction;
import webserver.api.ReadFile;
import webserver.api.registration.Registration;
import webserver.http.enums.Methods;

import java.util.HashMap;
import java.util.Map;

public class PathMap {
    private static Pathnode root;

    static{
        buildPathMap();
    }

        private static class Pathnode{
            String nodename;

            Map<Methods, ApiFunction> methods;

            Map<String, Pathnode> childnode;

        public Pathnode(String nodename) {
            this.nodename = nodename;
            this.methods = new HashMap<>();
            this.childnode = new HashMap<>();
            methods.put(Methods.GET , new ReadFile());
        }

        public ApiFunction getMethod(Methods method) {
            return methods.get(method);
        }

        public Pathnode getChild(String nodename) {
            return childnode.get(nodename);
        }

        public Pathnode addChild(String nodename) {
            Pathnode child = new Pathnode(nodename);
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

    public static Pathnode getRoot() {
        return root;
    }

    public static ApiFunction getPathMethod(Methods method, String path) {
        String[] routes = path.split("/");
        Pathnode current = root;
        if(routes.length == 1){
            return current.getMethod(method);
        }
        for(int i = 1; i < routes.length; i++){
            Pathnode next = current.getChild(routes[i]);
            if(next == null) break;
            current = next;
        }
        return current.getMethod(method);
    }


    private static void buildPathMap(){
        root = new Pathnode("root");

        //  /registration/~
        root.addChild("registration").addGetMethod(new Registration());
    }
}
